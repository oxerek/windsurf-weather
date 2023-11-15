package com.sonalake.windsurfweather.domain

import com.sonalake.windsurfweather.domain.ports.Client
import com.sonalake.windsurfweather.domain.ports.Repository
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import static com.sonalake.windsurfweather.domain.SpotLocations.values
import static reactor.test.StepVerifier.create

class FetchSpotForecastsUseCaseSpec extends BaseDomainSpec {

    def "should fetch and store forecasts for all spot locations"() {

        given:
        def client = Mock(Client)
        def repository = Mock(Repository)
        def useCase = new FetchSpotForecastsUseCase(client, repository)
        def locationsCount = values().size()
        def exampleForecastDto = spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore.toDto().get()

        when:
        locationsCount * client.get(_ as SpotDto) >> Flux.just(exampleForecastDto)
        locationsCount * repository.save(_ as SpotForecastDto) >> Mono.just(exampleForecastDto)

        def stepVerifier = create(useCase.execute()).expectNextCount(locationsCount).expectComplete()

        then:
        stepVerifier.verify()
    }
}
