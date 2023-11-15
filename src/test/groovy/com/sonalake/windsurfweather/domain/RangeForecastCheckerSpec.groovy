package com.sonalake.windsurfweather.domain

import static com.sonalake.windsurfweather.domain.SpotLocations.BRIDGETOWN
import static com.sonalake.windsurfweather.domain.SpotLocations.LE_MORNE

class RangeForecastCheckerSpec extends BaseDomainSpec {

    def "should find forecasts for given wind and temperature ranges"() {

        given:
        def checker = new RangeForecastChecker(ranges)
        def forecasts = [
                spotForecastBridgetownSuitableDayAndWindSpdAndLowScore,
                spotForecastFortalezaNotSuitable,
                spotForecastJastarniaNotSuitable,
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
