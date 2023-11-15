package com.sonalake.windsurfweather.domain;

import com.sonalake.windsurfweather.domain.ports.Client;
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static com.sonalake.windsurfweather.domain.SpotLocations.values;
import static java.util.stream.Stream.of;
import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Flux.fromStream;

@SuperBuilder
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BestSpotOnlineUseCase extends BestSpotUseCase {

    Client<SpotDto, SpotForecastDto> client;

    @Override
    Supplier<Flux<SpotForecastDto>> forecasts() {
        return () -> fromStream(of(values())
                .map(location -> location.toDto().get()))
                .flatMap(client::get);
    }
}
