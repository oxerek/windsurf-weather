package com.sonalake.windsurfweather.adapters.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpotForecastResponse {

    String locationName;
    String countryCode;
    String lon;
    String lat;
    String forecastDay;
    String avgTemp;
    String windSpd;
}
