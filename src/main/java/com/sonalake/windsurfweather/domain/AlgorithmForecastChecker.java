package com.sonalake.windsurfweather.domain;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.tuple.Pair.of;

@NoArgsConstructor
class AlgorithmForecastChecker implements Function<Set<SpotForecast>, Set<SpotForecast>> {

    @Override
    public Set<SpotForecast> apply(Set<SpotForecast> forecasts) {
        Function<Pair<SpotForecast, BigDecimal>, BigDecimal> score = Pair::getRight;
        return forecasts.stream()
                .map(forecast -> of(forecast, algorithm().apply(forecast.windSpd(), forecast.avgTemp())))
                .sorted(comparing(score).reversed())
                .limit(1)
                .map(Pair::getLeft)
                .collect(toSet());
    }

    BiFunction<BigDecimal, BigDecimal, BigDecimal> algorithm() {
        return (windSpd, temp) -> windSpd.multiply(new BigDecimal(3)).add(temp);
    }
}
