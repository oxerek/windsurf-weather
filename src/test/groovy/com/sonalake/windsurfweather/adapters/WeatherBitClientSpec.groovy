package com.sonalake.windsurfweather.adapters

import com.sonalake.windsurfweather.domain.ports.dto.SpotDto
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.test.StepVerifier

import static org.springframework.http.HttpStatus.OK
import static org.springframework.web.reactive.function.client.ClientResponse.create
import static org.springframework.web.reactive.function.client.WebClient.builder
import static reactor.core.publisher.Mono.just

class WeatherBitClientSpec extends BaseAdaptersSpec {

    def "should get forecasts from weatherBit service"() {

        given:
        def exchange = Mock(ExchangeFunction)
        def weatherClient = new WeatherBitClient(
                "apiKey",
                builder().exchangeFunction(exchange).build()
        )
        def spot = new SpotDto("Jastarnia", "PL")

        when:
        exchange.exchange(_ as ClientRequest) >> just(create(OK)
                .header("content-type", "application/json")
                .body(createJsonFromFile("jastarnia-forecast-day.json"))
                .build())

        def stepVerifier = StepVerifier.create(weatherClient.get(spot))
                .expectNextCount(16)
                .expectComplete()

        then:
        stepVerifier.verify()
    }
}
