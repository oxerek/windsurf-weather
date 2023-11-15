package com.sonalake.windsurfweather.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.Range;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.Range.between;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
class RangeForecastChecker implements Function<Set<SpotForecast>, Set<SpotForecast>> {

    ForecastCheckRanges ranges;

    @Override
    public Set<SpotForecast> apply(Set<SpotForecast> forecasts) {
        return forecasts.stream()
                .filter(notSuitable().negate())
                .collect(toSet());
    }

    private Predicate<SpotForecast> notSuitable() {
        return forecast -> !ranges.windSpdRange().contains(forecast.windSpd()) && !ranges.tempRange().contains(forecast.avgTemp());
    }

    @Builder
    @RequiredArgsConstructor(access = PRIVATE)
    @FieldDefaults(makeFinal = true, level = PRIVATE)
    static class ForecastCheckRanges {

        BigDecimal minWindSpd;
        BigDecimal maxWindSpd;
        BigDecimal minAvgTemp;
        BigDecimal maxAvgTemp;

        Range<BigDecimal> windSpdRange() {
            return between(minWindSpd, maxWindSpd);
        }

        Range<BigDecimal> tempRange() {
            return between(minAvgTemp, maxAvgTemp);
        }
    }
}
