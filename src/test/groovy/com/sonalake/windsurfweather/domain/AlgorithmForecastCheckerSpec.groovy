package com.sonalake.windsurfweather.domain

class AlgorithmForecastCheckerSpec extends BaseDomainSpec {

    def "should find best forecast by calculated scores"() {

        given:
        def checker = new AlgorithmForecastChecker()
        def forecasts = [
                spotForecastBridgetownSuitableDayAndWindSpdAndLowScore,
                spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore,
                fakeSpotForecastPreviousDayRelatedToWarsawAndSuitableWindSpdAndTempAndMediumScore
        ] as Set

        when:
        def results = checker.apply(forecasts)

        then:
        results.size() == 1
        results == [ spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore ] as Set
    }
}
