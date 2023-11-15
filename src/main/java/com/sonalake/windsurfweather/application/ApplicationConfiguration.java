package com.sonalake.windsurfweather.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.KubernetesConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.sonalake.windsurfweather.adapters.BestSpotHandler;
import com.sonalake.windsurfweather.adapters.FetchSpotForecastsHandler;
import com.sonalake.windsurfweather.adapters.InMemoryRepository;
import com.sonalake.windsurfweather.adapters.WeatherBitClient;
import com.sonalake.windsurfweather.adapters.model.SpotForecastEntity;
import com.sonalake.windsurfweather.domain.BestSpotOfflineUseCase;
import com.sonalake.windsurfweather.domain.BestSpotOnlineUseCase;
import com.sonalake.windsurfweather.domain.FetchSpotForecastsUseCase;
import com.sonalake.windsurfweather.domain.ports.Client;
import com.sonalake.windsurfweather.domain.ports.Repository;
import com.sonalake.windsurfweather.domain.ports.UseCase;
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.Duration;
import java.util.Map;

import static com.hazelcast.config.EvictionPolicy.NONE;
import static com.hazelcast.config.MaxSizePolicy.FREE_HEAP_SIZE;
import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static reactor.netty.http.client.HttpClient.create;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {

    @Profile("online")
    @Configuration
    public static class DomainOnlineConfiguration {

        @Bean
        UseCase<String, SpotForecastDto> bestSpotOnlineUseCase(
                ApplicationProperties properties,
                Client<SpotDto, SpotForecastDto> client
        ) {
            return BestSpotOnlineUseCase.builder()
                    .client(client)
                    .minAvgTemp(properties.getMinAvgTemp())
                    .maxAvgTemp(properties.getMaxAvgTemp())
                    .minWindSpd(properties.getMinWindSpd())
                    .maxWindSpd(properties.getMaxWindSpd())
                    .build();
        }
    }

    @Profile("offline")
    @Configuration
    public static class DomainOfflineConfiguration {

        @Bean
        UseCase<String, SpotForecastDto> bestSpotOfflineUseCase(
                ApplicationProperties properties,
                Repository<SpotForecastDto> repository
        ) {
            return BestSpotOfflineUseCase.builder()
                    .repository(repository)
                    .minAvgTemp(properties.getMinAvgTemp())
                    .maxAvgTemp(properties.getMaxAvgTemp())
                    .minWindSpd(properties.getMinWindSpd())
                    .maxWindSpd(properties.getMaxWindSpd())
                    .build();
        }

        @Bean
        UseCase<Void, SpotForecastDto> fetchSpotForecastsUseCase(
                Client<SpotDto, SpotForecastDto> client,
                Repository<SpotForecastDto> repository
        ) {
            return new FetchSpotForecastsUseCase(client, repository);
        }
    }

    @Configuration
    public static class AdaptersConfiguration {

        @Bean
        RouterFunction<ServerResponse> routes(BestSpotHandler bestSpotHandler) {
            return RouterFunctions.route()
                    .GET("/forecasts/best", bestSpotHandler::getBestSpotForecast)
                    .build();
        }

        @Bean
        BestSpotHandler spotForecastsHandler(UseCase<String, SpotForecastDto> bestSpotUseCase) {
            return new BestSpotHandler(bestSpotUseCase);
        }

        @Bean
        @Profile("offline")
        FetchSpotForecastsHandler fetchSpotForecastsHandler(
                UseCase<Void, SpotForecastDto> fetchSpotForecastsUseCase
        ) {
            return new FetchSpotForecastsHandler(fetchSpotForecastsUseCase);
        }
    }

    @Configuration
    public static class ClientConfiguration {

        @Bean
        Client<SpotDto, SpotForecastDto> client(
                WebClient webClient,
                ApplicationProperties properties
        ) {
            return new WeatherBitClient(properties.getWeatherBitApiKey(), webClient);
        }

        @Bean
        WebClient webClient(
                ApplicationProperties properties,
                ExchangeStrategies exchangeStrategies,
                ReactorClientHttpConnector httpConnector
        ) {
            return WebClient.builder()
                    .baseUrl(properties.getWeatherBitApiBaseUrl())
                    .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                    .clientConnector(httpConnector)
                    .exchangeStrategies(exchangeStrategies)
                    .build();
        }

        @Bean
        ExchangeStrategies exchangeStrategies(ObjectMapper objectMapper) {
            objectMapper.setPropertyNamingStrategy(new SnakeCaseStrategy());
            return ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                    .build();
        }

        @Bean
        ReactorClientHttpConnector httpConnector(ApplicationProperties properties) {
            return new ReactorClientHttpConnector(create()
                    .option(CONNECT_TIMEOUT_MILLIS, properties.getWeatherBitConnectTimeout())
                    .responseTimeout(Duration.ofMillis(properties.getWeatherBitResponseTimeout()))
                    .doOnConnected(connection -> connection
                            .addHandlerLast(new ReadTimeoutHandler(properties.getWeatherBitReadTimeout(), MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(properties.getWeatherBitWriteTimeout(), MILLISECONDS))));
        }
    }

    @Configuration
    public static class RepositoryConfiguration {

        @Bean
        Repository<SpotForecastDto> repository(Map<String, SpotForecastEntity> store) {
            return new InMemoryRepository(store);
        }

        @Bean
        Map<String, SpotForecastEntity> store(
                HazelcastInstance hazelcast,
                ApplicationProperties properties
        ) {
            return hazelcast.getMap(properties.getHazelcastCacheName());
        }

        @Bean
        Config hazelcastConfiguration(ApplicationProperties properties) {
            return new Config().setInstanceName(properties.getHazelcastInstanceName())
                    .setNetworkConfig(new NetworkConfig().setJoin(new JoinConfig()
                            .setKubernetesConfig(new KubernetesConfig().setEnabled(properties.isHazelcastKubernetes()))
                            .setMulticastConfig(new MulticastConfig().setEnabled(properties.isHazelcastMulticast())))
                    ).addMapConfig(new MapConfig()
                            .setName(properties.getHazelcastCacheName())
                            .setEvictionConfig(new EvictionConfig()
                                    .setEvictionPolicy(NONE)
                                    .setMaxSizePolicy(FREE_HEAP_SIZE))
                            .setTimeToLiveSeconds(properties.getHazelcastTtl()));
        }
    }
}
