package com.example.weathermcpdemo.controller;

import com.example.weathermcpdemo.model.WeatherResponse;
import com.example.weathermcpdemo.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for weather queries
 */
@RestController
@RequestMapping("/api/weather")
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * Get current weather for a city
     *
     * @param city The city name
     * @return Current weather data
     */
    @GetMapping("/current")
    public Mono<ResponseEntity<WeatherResponse>> getCurrentWeather(@RequestParam String city) {
        log.info("Received request for current weather in city: {}", city);
        return weatherService.getCurrentWeather(city)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Get weather forecast for a city
     *
     * @param city The city name
     * @param days Number of days for forecast (default: 5)
     * @return Weather forecast data
     */
    @GetMapping("/forecast")
    public Mono<ResponseEntity<WeatherResponse>> getWeatherForecast(
            @RequestParam String city,
            @RequestParam(defaultValue = "5") int days) {
        log.info("Received request for weather forecast in city: {} for {} days", city, days);
        return weatherService.getWeatherForecast(city, days)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Get weather analysis for a city using MCP
     *
     * @param city The city name
     * @return Weather analysis from MCP
     */
    @GetMapping("/analysis")
    public Mono<ResponseEntity<String>> getWeatherAnalysis(@RequestParam String city) {
        log.info("Received request for weather analysis in city: {}", city);
        return weatherService.getWeatherAnalysis(city)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Get weather forecast summary for a city using MCP
     *
     * @param city The city name
     * @param days Number of days for forecast (default: 5)
     * @return Weather forecast summary from MCP
     */
    @GetMapping("/forecast/summary")
    public Mono<ResponseEntity<String>> getWeatherForecastSummary(
            @RequestParam String city,
            @RequestParam(defaultValue = "5") int days) {
        log.info("Received request for weather forecast summary in city: {} for {} days", city, days);
        return weatherService.getWeatherForecastSummary(city, days)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
