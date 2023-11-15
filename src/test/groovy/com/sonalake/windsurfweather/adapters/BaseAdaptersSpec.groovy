package com.sonalake.windsurfweather.adapters

import com.sonalake.windsurfweather.domain.ports.dto.SpotDto
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto
import spock.lang.Specification

class BaseAdaptersSpec extends Specification {

    def spotForecastJastarniaDto = SpotForecastDto.builder()
            .spot(new SpotDto("Jastarnia", "PL"))
            .lon("-59.62021")
            .lat("13.10732")
            .forecastDay("2021-10-13")
            .avgTemp("30")
            .windSpd("18")
            .build()

    def spotForecastBridgetownDto = SpotForecastDto.builder()
            .spot(new SpotDto("Bridgetown", "BB"))
            .lon("18.67873")
            .lat("54.69606")
            .forecastDay("2021-10-13")
            .avgTemp("25")
            .windSpd("14")
            .build()

    static def createJsonFromFile(String jsonPath) {
        new File("src/test/resources/stubs/" + jsonPath).text
    }
}
