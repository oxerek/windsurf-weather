package com.sonalake.windsurfweather

import com.github.tomakehurst.wiremock.WireMockServer
import com.sonalake.windsurfweather.application.ApplicationInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

@TestPropertySource("classpath:integrationtest.properties")
@SpringBootTest(classes = ApplicationInitializer.class, webEnvironment = RANDOM_PORT)
class BaseIntegrationSpec extends Specification {

    @Autowired
    WebTestClient webTestClient

    @LocalServerPort
    Integer port

    @Shared
    def mockWeatherBitService = new WireMockServer()

    def setupSpec() {
        mockWeatherBitService.start()

        stubFor(get(urlPathEqualTo("/forecast/daily"))
                .willReturn(aResponse().withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(createJsonFromFile("jastarnia-forecast-day.json"))))
    }

    def cleanupSpec() {
        mockWeatherBitService.stop()
    }

    static def createJsonFromFile(String jsonPath) {
        new File("src/integration/resources/stubs/" + jsonPath).text
    }
}
