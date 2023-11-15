package com.sonalake.windsurfweather.domain;

import com.sonalake.windsurfweather.domain.ports.Client;
import com.sonalake.windsurfweather.domain.ports.Repository;
import com.sonalake.windsurfweather.domain.ports.UseCase;
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;

import static com.sonalake.windsurfweather.domain.SpotLocations.values;
import static java.util.stream.Stream.of;
import static lombok.AccessLevel.PRIVATE;
import static reactor.core.publisher.Flux.fromStream;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class FetchSpotForecastsUseCase implements UseCase<Void, SpotForecastDto> {

    Client<SpotDto, SpotForecastDto> client;
    Repository<SpotForecastDto> repository;

    @Override
    public Flux<SpotForecastDto> execute() {
        return fromStream(of(values())
                .map(location -> location.toDto().get()))
                .flatMap(client::get)
                .flatMap(repository::save);
    }
}
