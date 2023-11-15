package com.sonalake.windsurfweather.adapters;

import com.sonalake.windsurfweather.adapters.model.SpotForecastEntity;
import com.sonalake.windsurfweather.domain.ports.Repository;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.function.Function;

import static com.sonalake.windsurfweather.adapters.mapper.SpotForecastMapper.INSTANCE;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InMemoryRepository implements Repository<SpotForecastDto> {

    Scheduler scheduler = Schedulers.boundedElastic();

    Map<String, SpotForecastEntity> store;

    @Override
    public Flux<SpotForecastDto> findAll() {
        return Flux.fromStream(store.values().stream().map(INSTANCE::entityToDto));
    }

    @Override
    public Mono<SpotForecastDto> save(SpotForecastDto forecast) {
        return Mono.fromCallable(() -> store.compute(key().apply(forecast), (k, v) -> INSTANCE.dtoToEntity(forecast)))
                .map(INSTANCE::entityToDto)
                .subscribeOn(scheduler);
    }

    Function<SpotForecastDto, String> key() {
        return forecast -> forecast.getLocationName() + forecast.getCountryCode() + forecast.getForecastDay();
    }
}
