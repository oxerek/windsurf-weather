package com.sonalake.windsurfweather

import com.sonalake.windsurfweather.adapters.model.SpotForecastResponse
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("offline")
class BestSpotOfflineIntegrationSpec extends BaseIntegrationSpec {

    def "should request windsurf-weather service for best spot in offline mode"() {

        given:
        def day = "2021-10-22"

        when:
        def responseSpec = webTestClient.get().uri("http://localhost:" + port + ("/forecasts/best?day=" + day)).exchange()

        then:
        responseSpec.expectStatus().is2xxSuccessful()
        responseSpec.expectBody(SpotForecastResponse)
    }
}
