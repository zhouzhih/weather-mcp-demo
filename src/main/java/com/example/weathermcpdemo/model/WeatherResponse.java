package com.example.weathermcpdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private String city;
    private String country;
    private WeatherData current;
    private List<WeatherData> forecast;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeatherData {
        private String date;
        private double temperature;
        private double feelsLike;
        private double humidity;
        private String description;
        private double windSpeed;
        private String iconUrl;
    }
}
