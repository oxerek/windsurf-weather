package com.sonalake.windsurfweather.adapters.mapper;

import com.sonalake.windsurfweather.adapters.model.Forecast;
import com.sonalake.windsurfweather.adapters.model.SpotForecastEntity;
import com.sonalake.windsurfweather.adapters.model.SpotForecastResponse;
import com.sonalake.windsurfweather.domain.ports.dto.SpotDto;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpotForecastMapper {

    SpotForecastMapper INSTANCE = Mappers.getMapper(SpotForecastMapper.class);

    @Mapping(target = "locationName", expression = "java(spotForecastDto.getSpot().getLocationName())")
    @Mapping(target = "countryCode", expression = "java(spotForecastDto.getSpot().getCountryCode())")
    SpotForecastEntity dtoToEntity(SpotForecastDto spotForecastDto);

    @Mapping(target = "spot", expression = "java(new SpotDto(spotForecastEntity.getLocationName(), spotForecastEntity.getCountryCode()))")
    SpotForecastDto entityToDto(SpotForecastEntity spotForecastEntity);

    @Mapping(target = "spot", expression = "java(spotDto)")
    @Mapping(target = "lon", expression = "java(lon)")
    @Mapping(target = "lat", expression = "java(lat)")
    @Mapping(target = "forecastDay", expression = "java(response.getDatetime())")
    @Mapping(target = "avgTemp", expression = "java(response.getTemp().toPlainString())")
    SpotForecastDto clientResponseToDto(SpotDto spotDto, String lon, String lat, Forecast response);

    @Mapping(target = "locationName", expression = "java(spotForecastDto.getSpot().getLocationName())")
    @Mapping(target = "countryCode", expression = "java(spotForecastDto.getSpot().getCountryCode())")
    SpotForecastResponse dtoToServerResponse(SpotForecastDto spotForecastDto);
}
