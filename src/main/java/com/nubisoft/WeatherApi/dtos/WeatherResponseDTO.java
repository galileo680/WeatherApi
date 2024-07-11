package com.nubisoft.WeatherApi.dtos;

public class WeatherResponseDTO {
    private double temperature;
    private String condition;
    private String city;

    public WeatherResponseDTO(double temperature, String condition, String city) {
        this.temperature = temperature;
        this.condition = condition;
        this.city = city;
    }

    // Getters and setters
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}