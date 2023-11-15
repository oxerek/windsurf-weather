package com.sonalake.windsurfweather.domain;

import com.sonalake.windsurfweather.domain.ports.Repository;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@SuperBuilder
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BestSpotOfflineUseCase extends BestSpotUseCase {

    Repository<SpotForecastDto> repository;

    @Override
    Supplier<Flux<SpotForecastDto>> forecasts() {
        return repository::findAll;
    }
}
