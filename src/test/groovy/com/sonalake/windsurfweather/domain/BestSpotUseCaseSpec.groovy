package com.sonalake.windsurfweather.domain

import com.sonalake.windsurfweather.domain.ports.Client
import com.sonalake.windsurfweather.domain.ports.Repository
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto
import reactor.test.StepVerifier
import reactor.test.publisher.TestPublisher

class BestSpotUseCaseSpec extends BaseDomainSpec {

    def "should find best forecast offline"() {

        given:
        def testPublisher = TestPublisher.create()
        def repository = Mock(Repository)

        def useCase = BestSpotOfflineUseCase.builder()
                .repository(repository)
                .minWindSpd(ranges.windSpdRange().getMinimum())
                .maxWindSpd(ranges.windSpdRange().getMaximum())
                .minAvgTemp(ranges.tempRange().getMaximum())
                .maxAvgTemp(ranges.tempRange().getMaximum())
                .build()

        def forecasts = [
                spotForecastBridgetownSuitableDayAndWindSpdAndLowScore.toDto().get(),
                spotForecastFortalezaNotSuitable.toDto().get(),
                spotForecastJastarniaNotSuitable.toDto().get(),
                fakeSpotForecastPreviousDayRelatedToWarsawAndSuitableWindSpdAndTempAndMediumScore.toDto().get(),
                spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore.toDto().get(),
                spotForecastPissouriNotSuitable.toDto().get()
        ] as SpotForecastDto[]

        when:
        repository.findAll() >> testPublisher.flux()

        def stepVerifier = StepVerifier.create(useCase.execute(day))
                .then({ testPublisher.emit(forecasts) })
                .expectNext(spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore.toDto().get())
                .expectComplete()

        then:
        stepVerifier.verify()
    }

    def "should find best forecast online"() {

        given:
        def testPublisher = TestPublisher.create()
        def client = Mock(Client)

        def useCase = BestSpotOnlineUseCase.builder()
                .client(client)
                .minWindSpd(ranges.windSpdRange().getMinimum())
                .maxWindSpd(ranges.windSpdRange().getMaximum())
                .minAvgTemp(ranges.tempRange().getMaximum())
                .maxAvgTemp(ranges.tempRange().getMaximum())
                .build()

        def forecasts = [
                spotForecastBridgetownSuitableDayAndWindSpdAndLowScore.toDto().get(),
                spotForecastFortalezaNotSuitable.toDto().get(),
                spotForecastJastarniaNotSuitable.toDto().get(),
                fakeSpotForecastPreviousDayRelatedToWarsawAndSuitableWindSpdAndTempAndMediumScore.toDto().get(),
                spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore.toDto().get(),
                spotForecastPissouriNotSuitable.toDto().get()
        ] as SpotForecastDto[]

        when:
        client.get(_ as SpotDto) >> testPublisher.flux()

        def stepVerifier = StepVerifier.create(useCase.execute(day))
                .then({ testPublisher.emit(forecasts) })
                .expectNext(spotForecastLeMorneSuitableDayAndWindSpdAndTempAndBestScore.toDto().get())
                .expectComplete()

        then:
        stepVerifier.verify()
    }
}
