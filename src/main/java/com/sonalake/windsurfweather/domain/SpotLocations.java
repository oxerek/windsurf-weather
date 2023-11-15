package com.sonalake.windsurfweather.domain;

import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import java.lang.String;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Accessors(
        fluent = true
)
@FieldDefaults(
        makeFinal = true,
        level = AccessLevel.PRIVATE
)
@RequiredArgsConstructor
public enum SpotLocations {
    JASTARNIA("Jastarnia", "PL", new BigDecimal("18.67873"), new BigDecimal("54.69606"), ZoneId.of("Europe/Warsaw")),

    BRIDGETOWN("Bridgetown", "BB", new BigDecimal("-59.62021"), new BigDecimal("13.10732"), ZoneId.of("America/Barbados")),

    FORTALEZA("Fortaleza", "BR", new BigDecimal("-38.54306"), new BigDecimal("-3.71722"), ZoneId.of("America/Fortaleza")),

    PISSOURI("Pissouri", "CY", new BigDecimal("32.70132"), new BigDecimal("34.66942"), ZoneId.of("Asia/Nicosia")),

    LE_MORNE("Le Morne", "MQ", new BigDecimal("-61"), new BigDecimal("14.7"), ZoneId.of("America/Martinique"));

    String cityName;

    String countryCode;

    BigDecimal lon;

    BigDecimal lat;

    ZoneId timezone;

    static Supplier<Optional<SpotLocations>> of(String cityName, String countryCode) {
        return () -> java.util.stream.Stream.of(values())
                .filter(location -> location.cityName.equalsIgnoreCase(cityName) && location.countryCode.equalsIgnoreCase(countryCode))
                .findFirst();
    }

    Supplier<SpotDto> toDto() {
        return () -> new SpotDto(cityName(), countryCode());
    }
}
