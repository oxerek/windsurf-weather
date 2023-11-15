package com.sonalake.windsurfweather.domain

class CombinedForecastCheckersSpec extends BaseDomainSpec {

    def "should find best forecast"() {

        given:
        def combinedCheckers = new DayForecastChecker(forecastDay)
                .andThen(new RangeForecastChecker(ranges))
                .andThen(new AlgorithmForecastChecker())
        def forecasts = [
                spotForecastBridgetownSuitableDayAndWindSpdAndLowScore,
                spotForecastFortalezaNotSuitable,
                spotForecastJastarniaNotSuitable,
                fakeSpotForecastPreviousDayRelatedToWarsawAndSuitableWindSpdAndTempAndMediumScore,
                spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore,
                spotForecastPissouriNotSuitable
        ] as Set

        when:
        def results = combinedCheckers.apply(forecasts)

        then:
        results.size() == 1
        results == [ spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore ] as Set
    }
}
