package com.sonalake.windsurfweather.adapters

import com.sonalake.windsurfweather.adapters.model.SpotForecastEntity
import reactor.test.StepVerifier

class InMemoryRepositorySpec extends BaseAdaptersSpec {

    def "should save entities and then find them all"() {

        given:
        def store = [:] as Map<String, SpotForecastEntity>
        def repository = new InMemoryRepository(store)

        when:
        def saveJastarniaDtoVerifier = StepVerifier.create(repository.save(spotForecastJastarniaDto))
                .expectNext(spotForecastJastarniaDto)
                .expectComplete()

        and:
        def saveBridgetownDtoVerifier = StepVerifier.create(repository.save(spotForecastBridgetownDto))
                .expectNext(spotForecastBridgetownDto)
                .expectComplete()

        and:
        def findAllVerifier = StepVerifier.create(repository.findAll())
                .expectNext(spotForecastJastarniaDto)
                .expectNext(spotForecastBridgetownDto)
                .expectComplete()

        then:
        saveJastarniaDtoVerifier.verify()
        saveBridgetownDtoVerifier.verify()
        findAllVerifier.verify()
    }
}
