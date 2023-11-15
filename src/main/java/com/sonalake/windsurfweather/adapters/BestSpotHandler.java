package com.sonalake.windsurfweather.adapters;

import com.sonalake.windsurfweather.adapters.model.SpotForecastResponse;
import com.sonalake.windsurfweather.domain.ports.UseCase;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.sonalake.windsurfweather.adapters.mapper.SpotForecastMapper.INSTANCE;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BestSpotHandler {

    UseCase<String, SpotForecastDto> bestSpotUseCase;

    public Mono<ServerResponse> getBestSpotForecast(ServerRequest request) {
        return bestSpotUseCase.execute(dayQueryParameter(request))
                .map(INSTANCE::dtoToServerResponse)
                .flatMap(spotForecastResponse -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(spotForecastResponse), SpotForecastResponse.class))
                        .switchIfEmpty(noContent().build());
    }

    private String dayQueryParameter(ServerRequest request) {
        return request.queryParam("day").orElse(now().format(ISO_LOCAL_DATE));
    }
}
