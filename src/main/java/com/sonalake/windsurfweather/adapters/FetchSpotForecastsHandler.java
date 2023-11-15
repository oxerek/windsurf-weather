package com.sonalake.windsurfweather.adapters;

import com.sonalake.windsurfweather.domain.ports.UseCase;
import com.sonalake.windsurfweather.domain.ports.dto.SpotForecastDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

@Log
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FetchSpotForecastsHandler {

    UseCase<Void, SpotForecastDto> fetchSpotForecastsUseCase;

    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {

        log.info("Fetching data on: " + event);

        fetchSpotForecastsUseCase.execute().subscribe();
    }

    @Scheduled(cron = "${windsurf-weather.fetchSpotForecastsCron}")
    public void fetchSpotForecasts() {

        log.info("Fetching data");

        fetchSpotForecastsUseCase.execute().subscribe();
    }
}
