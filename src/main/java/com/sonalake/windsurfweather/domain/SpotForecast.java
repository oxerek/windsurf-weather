package com.sonalake.windsurfweather.domain;

import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.sonalake.windsurfweather.domain.SpotLocations.of;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Accessors(fluent = true)
@Builder
@RequiredArgsConstructor(access = PRIVATE)
@FieldDefaults(makeFinal = true, level = PRIVATE)
class SpotForecast {

    ZonedDateTime day;
    SpotLocations location;
    BigDecimal windSpd;
    BigDecimal avgTemp;

    Predicate<ZonedDateTime> forDay(Function<ZonedDateTime, LocalDate> dayExtractor) {
        return forDay -> dayExtractor.apply(day).isEqual(dayExtractor.apply(forDay));
    }

    static Function<SpotForecastDto, SpotForecast> ofDto() {
        return dto -> {
            var location = of(dto.getLocationName(), dto.getCountryCode()).get().orElseThrow();
            return SpotForecast.builder()
                    .day(ZonedDateTime.of(LocalDate.parse(dto.getForecastDay()).atStartOfDay(), location.timezone()))
                    .location(location)
                    .windSpd(new BigDecimal(dto.getWindSpd()))
                    .avgTemp(new BigDecimal(dto.getAvgTemp()))
                    .build();
        };
    }

    Supplier<SpotForecastDto> toDto() {
        return () -> SpotForecastDto.builder()
                .forecastDay(day.format(ISO_LOCAL_DATE))
                .spot(new SpotDto(location.cityName(), location.countryCode()))
                .lon(location.lon().toPlainString())
                .lat(location.lat().toPlainString())
                .avgTemp(avgTemp.toPlainString())
                .windSpd(windSpd.toPlainString())
                .build();
    }
}
