package com.sonalake.windsurfweather.domain.ports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
@Builder
@AllArgsConstructor
public class SpotForecastDto {

    @Delegate
    SpotDto spot;
    String lon;
    String lat;
    String forecastDay;
    String avgTemp;
    String windSpd;
}
