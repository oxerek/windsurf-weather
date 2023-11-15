package com.sonalake.windsurfweather.domain

import spock.lang.Specification

import static com.sonalake.windsurfweather.domain.SpotForecast.builder
import static com.sonalake.windsurfweather.domain.SpotLocations.BRIDGETOWN
import static com.sonalake.windsurfweather.domain.SpotLocations.FORTALEZA
import static com.sonalake.windsurfweather.domain.SpotLocations.JASTARNIA
import static com.sonalake.windsurfweather.domain.SpotLocations.LE_MORNE
import static com.sonalake.windsurfweather.domain.SpotLocations.PISSOURI
import static java.time.LocalDate.parse
import static java.time.ZoneId.of

class BaseDomainSpec extends Specification {

    def timezone = of("Europe/Warsaw")

    def day = "2021-10-13"

    def forecastDay = parse(day).atStartOfDay(timezone)

    def ranges = RangeForecastChecker.ForecastCheckRanges.builder()
            .minAvgTemp(new BigDecimal(5))
            .maxAvgTemp(new BigDecimal(35))
            .minWindSpd(new BigDecimal(5))
            .maxWindSpd(new BigDecimal(18))
            .build()

    def spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore = builder()
            .day(parse("2021-10-13").atStartOfDay(LE_MORNE.timezone()))
            .location(LE_MORNE)
            .windSpd(new BigDecimal("18.00000"))
            .avgTemp(new BigDecimal("35.0"))
            .build()

    def spotForecastBridgetownSuitableDayAndWindSpdAndLowScore = builder()
            .day(parse("2021-10-13").atStartOfDay(BRIDGETOWN.timezone()))
            .location(BRIDGETOWN)
            .windSpd(new BigDecimal("6.41933"))
            .avgTemp(new BigDecimal("4.5"))
            .build()

    def fakeSpotForecastPreviousDayRelatedToWarsawAndSuitableWindSpdAndTempAndMediumScore = builder()
            .day(parse("2021-10-13").atStartOfDay(of("Australia/Sydney")))
            .location(PISSOURI)
            .windSpd(new BigDecimal("17.23434"))
            .avgTemp(new BigDecimal("28.1"))
            .build()

    def spotForecastFortalezaNotSuitable = builder()
            .day(parse("2021-10-15").atStartOfDay(FORTALEZA.timezone()))
            .location(FORTALEZA)
            .windSpd(new BigDecimal("18.00001"))
            .avgTemp(new BigDecimal("4.9"))
            .build()

    def spotForecastJastarniaNotSuitable = builder()
            .day(parse("2021-10-11").atStartOfDay(JASTARNIA.timezone()))
            .location(JASTARNIA)
            .windSpd(new BigDecimal("1.00000"))
            .avgTemp(new BigDecimal("-3.9"))
            .build()

    def spotForecastPissouriNotSuitable = builder()
            .day(parse("2021-10-01").atStartOfDay(PISSOURI.timezone()))
            .location(PISSOURI)
            .windSpd(new BigDecimal("23.13222"))
            .avgTemp(new BigDecimal("36.7"))
            .build()
}
