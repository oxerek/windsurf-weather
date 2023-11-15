package com.sonalake.windsurfweather.domain.ports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpotDto {

    String locationName;
    String countryCode;
}
