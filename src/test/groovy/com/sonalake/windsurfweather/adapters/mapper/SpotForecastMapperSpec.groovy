package com.sonalake.windsurfweather.adapters.mapper

import com.sonalake.windsurfweather.adapters.BaseAdaptersSpec
import com.sonalake.windsurfweather.adapters.model.Forecast
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto

import static com.sonalake.windsurfweather.adapters.mapper.SpotForecastMapper.INSTANCE

class SpotForecastMapperSpec extends BaseAdaptersSpec {

    def "should map dto and entity back and forth"() {

        when:
        def entity = INSTANCE.dtoToEntity(spotForecastJastarniaDto)

        then:
        entity.getLocationName() == spotForecastJastarniaDto.getSpot().getLocationName()
        entity.getCountryCode() == spotForecastJastarniaDto.getSpot().getCountryCode()
        entity.getAvgTemp() == spotForecastJastarniaDto.getAvgTemp()
        entity.getForecastDay() == spotForecastJastarniaDto.getForecastDay()
        entity.getWindSpd() == spotForecastJastarniaDto.getWindSpd()
        entity.getLon() == spotForecastJastarniaDto.getLon()
        entity.getLat() == spotForecastJastarniaDto.getLat()

        and:
        def dto = INSTANCE.entityToDto(entity)

        then:
        dto == spotForecastJastarniaDto
    }

    def "should map client response to dto"() {

        given:
        def response = new Forecast()
        response.setDatetime("2021-10-13")
        response.setTemp(new BigDecimal("30"))
        response.setWindSpd(new BigDecimal("18"))

        when:
        def result = INSTANCE.clientResponseToDto(
                new SpotDto("Jastarnia", "PL"),
                "-59.62021",
                "13.10732",
                response
        )

        then:
        result == spotForecastJastarniaDto
    }
}
