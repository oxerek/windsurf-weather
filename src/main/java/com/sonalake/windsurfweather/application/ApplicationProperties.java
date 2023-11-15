package com.sonalake.windsurfweather.application;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@ConfigurationProperties(prefix = "windsurf-weather")
@FieldDefaults(level = PRIVATE)
public class ApplicationProperties {

    BigDecimal minWindSpd;
    BigDecimal maxWindSpd;
    BigDecimal minAvgTemp;
    BigDecimal maxAvgTemp;

    String weatherBitApiBaseUrl;
    String weatherBitApiKey;

    int weatherBitConnectTimeout;
    long weatherBitResponseTimeout;
    long weatherBitWriteTimeout;
    long weatherBitReadTimeout;

    String hazelcastInstanceName;
    String hazelcastCacheName;
    int hazelcastTtl;
    boolean hazelcastMulticast;
    boolean hazelcastKubernetes;
}
