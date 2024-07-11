package com.nubisoft.WeatherApi.service;

import com.nubisoft.WeatherApi.dtos.WeatherResponseDTO;
import com.nubisoft.WeatherApi.entity.WeatherRecord;
import com.nubisoft.WeatherApi.models.Current;
import com.nubisoft.WeatherApi.models.ForecastData;
import com.nubisoft.WeatherApi.models.ForecastDay;
import com.nubisoft.WeatherApi.models.WeatherData;
import com.nubisoft.WeatherApi.repositories.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final WebClient.Builder webClientBuilder;
    private final WeatherRepository weatherRepository;

    @Value("${weather.api.key}")
    private String apiKey;
    @Value("${weather.api.url}")
    private  String baseUrl;

    public WeatherService(WebClient.Builder webClientBuilder,
                          WeatherRepository weatherRepository) {
        this.webClientBuilder = webClientBuilder;
        this.weatherRepository = weatherRepository;
    }

    public List<WeatherResponseDTO> getRealtimeWeather()
    {
        List<WeatherResponseDTO> result = new ArrayList<>();

        WeatherData gliwiceData = getCurrentWeather("Gliwice");
        result.add(new WeatherResponseDTO(
                gliwiceData.getCurrent().getTemp_c(),
                gliwiceData.getCurrent().getCondition().getText(),
                "Gliwice"
        ));

        WeatherData hamburgData = getCurrentWeather("Hamburg");
        result.add(new WeatherResponseDTO(
                hamburgData.getCurrent().getTemp_c(),
                hamburgData.getCurrent().getCondition().getText(),
                "Hamburg"
        ));

        return result;
    }

    public WeatherData getCurrentWeather(String city) {
        String url = baseUrl + "/current.json?key=" + apiKey + "&q=" + city;
        WeatherData weatherData = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(WeatherData.class)
                .block();
        saveWeatherData(city, weatherData);
        return weatherData;
    }


    public List<WeatherResponseDTO> getForecastWeather() {
        String city1 = "Gliwice";
        String city2 = "Hamburg";
        int days = 7;

        List<WeatherResponseDTO> forecastForGliwice = getWeatherForecastForCity(city1, days);
        List<WeatherResponseDTO> forecastForHamburg = getWeatherForecastForCity(city2, days);

        List<WeatherResponseDTO> combinedForecast = new ArrayList<>();
        combinedForecast.addAll(forecastForGliwice);
        combinedForecast.addAll(forecastForHamburg);

        return combinedForecast;
    }

    private List<WeatherResponseDTO> getWeatherForecastForCity(String city, int days) {
        String url = baseUrl + "/forecast.json?key=" + apiKey + "&q=" + city + "&days=" + days;
        ForecastData forecastData = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ForecastData.class)
                .block();

        return forecastData.getForecast().getForecastday().stream()
                .map(forecastDay -> mapToWeatherResponseDTO(forecastDay, city))
                .collect(Collectors.toList());
    }


    private WeatherResponseDTO mapToWeatherResponseDTO(ForecastDay forecastDay, String city) {
        return new WeatherResponseDTO(
                forecastDay.getDay().getMaxtemp_c(),
                forecastDay.getDay().getCondition().getText(),
                city
        );
    }

    private void saveWeatherData(String city, WeatherData weatherData) {
        WeatherRecord record = new WeatherRecord();
        record.setCity(city);

        Current current = weatherData.getCurrent();
        record.setTemperature(current.getTemp_c());
        record.setCondition(current.getCondition().getText());

        record.setTimestamp(LocalDateTime.now());
        weatherRepository.save(record);
    }

}