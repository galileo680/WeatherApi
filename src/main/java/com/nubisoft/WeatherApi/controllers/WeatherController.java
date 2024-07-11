package com.nubisoft.WeatherApi.controllers;

import com.nubisoft.WeatherApi.dtos.WeatherResponseDTO;
import com.nubisoft.WeatherApi.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/realtime-weather")
    public List<WeatherResponseDTO> getRealtimeWeather() { return weatherService.getRealtimeWeather(); }

    @GetMapping("/forecast-weather")
    public List<WeatherResponseDTO> getForecastWeather() {
        return weatherService.getForecastWeather();
    }
}
