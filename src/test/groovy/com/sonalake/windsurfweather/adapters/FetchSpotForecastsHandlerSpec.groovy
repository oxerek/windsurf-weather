package com.sonalake.windsurfweather.adapters

import com.sonalake.windsurfweather.domain.FetchSpotForecastsUseCase
import org.springframework.boot.context.event.ApplicationStartedEvent
import reactor.core.publisher.Flux
import spock.lang.Specification

class FetchSpotForecastsHandlerSpec extends Specification {

    def "should invoke FetchSpotForecastsUseCase"() {

        given:
        def useCase = Mock(FetchSpotForecastsUseCase)
        def handler = new FetchSpotForecastsHandler(useCase)

        when:
        1 * useCase.execute() >> Flux.empty()
        handler.fetchSpotForecasts()

        then:
        true
    }

    def "should invoke FetchSpotForecastsUseCase on ApplicationStartedEvent"() {

        given:
        def event = Mock(ApplicationStartedEvent)
        def useCase = Mock(FetchSpotForecastsUseCase)
        def handler = new FetchSpotForecastsHandler(useCase)

        when:
        1 * useCase.execute() >> Flux.empty()
        handler.onApplicationEvent(event)

        then:
        true
    }
}
