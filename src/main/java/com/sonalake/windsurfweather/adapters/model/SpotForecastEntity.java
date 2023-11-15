package com.sonalake.windsurfweather.adapters.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SpotForecastEntity implements Serializable {

    String locationName;
    String countryCode;
    String lon;
    String lat;
    String forecastDay;
    String avgTemp;
    String windSpd;
}
