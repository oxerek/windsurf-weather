package com.sonalake.windsurfweather.adapters

import com.sonalake.windsurfweather.domain.BestSpotOfflineUseCase
import org.springframework.mock.web.reactive.function.server.MockServerRequest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.springframework.http.HttpStatus.OK

class BestSpotHandlerSpec extends BaseAdaptersSpec {

    def "should response with 200 http status"() {

        given:
        def request = MockServerRequest.builder().build()
        def useCase = Mock(BestSpotOfflineUseCase)
        def handler = new BestSpotHandler(useCase)

        when:
        1 * useCase.execute(_) >> Mono.just(spotForecastJastarniaDto)
        def stepVerifier = StepVerifier.create(handler.getBestSpotForecast(request))
                .expectNextMatches(response -> response.statusCode() == OK)
                .expectComplete()

        then:
        stepVerifier.verify()
    }
}
