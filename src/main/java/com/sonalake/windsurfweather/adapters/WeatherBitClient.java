package com.sonalake.windsurfweather.adapters;

import com.sonalake.windsurfweather.adapters.model.ForecastDay;
import com.sonalake.windsurfweather.domain.ports.Client;
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.function.Function;

import static com.sonalake.windsurfweather.adapters.mapper.SpotForecastMapper.INSTANCE;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.fromIterable;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WeatherBitClient implements Client<SpotDto, SpotForecastDto> {

    String apiKey;
    WebClient webClient;

    @Override
    public Flux<SpotForecastDto> get(SpotDto spotDto) {
         return webClient.get()
                 .uri(dailyForecastUri(spotDto))
                 .retrieve()
                 .bodyToMono(ForecastDay.class)
                 .flatMapMany(forecasts(spotDto))
                 .share();
    }

    private Function<ForecastDay, Flux<SpotForecastDto>> forecasts(SpotDto spotDto) {
        return forecastDay -> forecastDay.getData() != null ? fromIterable(forecastDay.getData())
                .map(forecast -> INSTANCE.clientResponseToDto(spotDto, forecastDay.getLon(), forecastDay.getLat(), forecast)) : empty();
    }

    private Function<UriBuilder, URI> dailyForecastUri(SpotDto spotDto) {
        return uriBuilder -> uriBuilder.path("forecast/daily")
                .queryParam("city", spotDto.getLocationName())
                .queryParam("country", spotDto.getCountryCode())
                .queryParam("key", apiKey)
                .build();
    }
}
