package com.sonalake.windsurfweather.domain

import static com.sonalake.windsurfweather.domain.SpotLocations.BRIDGETOWN
import static com.sonalake.windsurfweather.domain.SpotLocations.LE_MORNE

class DayForecastCheckerSpec extends BaseDomainSpec {

    def "should find forecasts for given local day"() {

        given:
        def checker = new DayForecastChecker(forecastDay)
        def forecasts = [
                spotForecastBridgetownSuitableDayAndWindSpdAndLowScore,
                spotForecastFortalezaNotSuitable,
                spotForecastJastarniaNotSuitable,
                fakeSpotForecastPreviousDayRelatedToWarsawAndSuitableWindSpdAndTempAndMediumScore,
                spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore,
                spotForecastPissouriNotSuitable
        ] as Set

        when:
        def results = checker.apply(forecasts)

        then:
        results.size() == 2
        results.any {it.location() == BRIDGETOWN }
        results.any {it.location() == LE_MORNE }
    }
}
