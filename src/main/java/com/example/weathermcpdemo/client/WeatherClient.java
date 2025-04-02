package com.example.weathermcpdemo.client;

import com.example.weathermcpdemo.model.WeatherResponse;
import reactor.core.publisher.Mono;

/**
 * Client interface for retrieving weather data
 */
public interface WeatherClient {
    
    /**
     * Get current weather data for a specific city
     * 
     * @param city The city name
     * @return Weather response containing current weather data
     */
    Mono<WeatherResponse> getCurrentWeather(String city);
    
    /**
     * Get weather forecast for a specific city
     * 
     * @param city The city name
     * @param days Number of days for forecast
     * @return Weather response containing forecast data
     */
    Mono<WeatherResponse> getWeatherForecast(String city, int days);
}
