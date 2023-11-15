package com.sonalake.windsurfweather.domain;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Function;

import static java.time.LocalDate.ofInstant;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
class DayForecastChecker implements Function<Set<SpotForecast>, Set<SpotForecast>> {
    
    ZonedDateTime day;

    @Override
    public Set<SpotForecast> apply(Set<SpotForecast> forecasts) {
        return forecasts.stream()
                .filter(forecast -> forecast.forDay(localDay()).test(day))
                .collect(toSet());
    }

    Function<ZonedDateTime, LocalDate> localDay() {
        return zonedDay -> ofInstant(zonedDay.toInstant(), day.getZone());
    }
}
