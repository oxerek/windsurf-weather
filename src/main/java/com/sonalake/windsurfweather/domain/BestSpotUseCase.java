package com.sonalake.windsurfweather.domain;

import com.sonalake.windsurfweather.domain.ports.UseCase;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.sonalake.windsurfweather.domain.RangeForecastChecker.ForecastCheckRanges;
import static com.sonalake.windsurfweather.domain.SpotForecast.ofDto;
import static java.time.LocalDate.parse;
import static java.time.ZoneId.systemDefault;
import static java.time.ZonedDateTime.of;
import static lombok.AccessLevel.PRIVATE;

@SuperBuilder
@RequiredArgsConstructor(access = PRIVATE)
@FieldDefaults(makeFinal = true, level = PRIVATE)
abstract class BestSpotUseCase implements UseCase<String, SpotForecastDto> {

    BigDecimal minWindSpd;
    BigDecimal maxWindSpd;
    BigDecimal minAvgTemp;
    BigDecimal maxAvgTemp;

    @Override
    public Mono<SpotForecastDto> execute(String day) {

        var forecastForDay = of(parse(day).atStartOfDay(), systemDefault());

        var ranges = ForecastCheckRanges.builder()
                .minWindSpd(minWindSpd)
                .maxWindSpd(maxWindSpd)
                .minAvgTemp(minAvgTemp)
                .maxAvgTemp(maxAvgTemp)
                .build();

        var combinedCheckers = new DayForecastChecker(forecastForDay)
                .andThen(new RangeForecastChecker(ranges))
                .andThen(new AlgorithmForecastChecker());

        return forecasts().get()
                .map(dto -> ofDto().apply(dto))
                .collectList()
                .map(bestSpot(combinedCheckers))
                .flatMap(Mono::justOrEmpty);
    }

    abstract Supplier<Flux<SpotForecastDto>> forecasts();

    private Function<List<SpotForecast>, Optional<SpotForecastDto>> bestSpot(Function<Set<SpotForecast>, Set<SpotForecast>> combinedCheckers) {
        return forecasts -> combinedCheckers.apply(new HashSet<>(forecasts))
                .stream()
                .findFirst()
                .map(forecast -> forecast.toDto().get());
    }
}
