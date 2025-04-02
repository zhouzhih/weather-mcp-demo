package com.example.weathermcpdemo.service;

import com.example.weathermcpdemo.client.AlibabaBailianMcpClient;
import com.example.weathermcpdemo.client.WeatherClient;
import com.example.weathermcpdemo.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Service for retrieving and analyzing weather data
 */
@Service
@Slf4j
public class WeatherService {

    private final WeatherClient weatherClient;
    private final AlibabaBailianMcpClient mcpClient;

    @Autowired
    public WeatherService(WeatherClient weatherClient, AlibabaBailianMcpClient mcpClient) {
        this.weatherClient = weatherClient;
        this.mcpClient = mcpClient;
    }

    /**
     * Get current weather data for a specific city
     *
     * @param city The city name
     * @return Weather response containing current weather data
     */
    public Mono<WeatherResponse> getCurrentWeather(String city) {
        log.info("Retrieving current weather for city: {}", city);
        return weatherClient.getCurrentWeather(city);
    }

    /**
     * Get weather forecast for a specific city
     *
     * @param city The city name
     * @param days Number of days for forecast
     * @return Weather response containing forecast data
     */
    public Mono<WeatherResponse> getWeatherForecast(String city, int days) {
        log.info("Retrieving weather forecast for city: {} for {} days", city, days);
        return weatherClient.getWeatherForecast(city, days);
    }

    /**
     * Get weather analysis for a specific city using MCP
     *
     * @param city The city name
     * @return Weather analysis from MCP
     */
    public Mono<String> getWeatherAnalysis(String city) {
        log.info("Generating weather analysis for city: {}", city);
        return weatherClient.getCurrentWeather(city)
                .flatMap(weatherResponse -> {
                    String weatherData = String.format(
                            "City: %s, Country: %s\nTemperature: %.1f°C, Feels like: %.1f°C\nHumidity: %.1f%%\nDescription: %s\nWind speed: %.1f m/s",
                            weatherResponse.getCity(),
                            weatherResponse.getCountry(),
                            weatherResponse.getCurrent().getTemperature(),
                            weatherResponse.getCurrent().getFeelsLike(),
                            weatherResponse.getCurrent().getHumidity(),
                            weatherResponse.getCurrent().getDescription(),
                            weatherResponse.getCurrent().getWindSpeed()
                    );
                    
                    CompletableFuture<String> analysisFuture = mcpClient.generateWeatherAnalysis(weatherData);
                    return Mono.fromFuture(analysisFuture);
                });
    }

    /**
     * Get weather forecast summary for a specific city using MCP
     *
     * @param city The city name
     * @param days Number of days for forecast
     * @return Weather forecast summary from MCP
     */
    public Mono<String> getWeatherForecastSummary(String city, int days) {
        log.info("Generating weather forecast summary for city: {} for {} days", city, days);
        return weatherClient.getWeatherForecast(city, days)
                .flatMap(weatherResponse -> {
                    StringBuilder forecastData = new StringBuilder();
                    forecastData.append(String.format("City: %s, Country: %s\n\n", weatherResponse.getCity(), weatherResponse.getCountry()));
                    forecastData.append("Forecast:\n");
                    
                    weatherResponse.getForecast().forEach(forecast -> {
                        forecastData.append(String.format(
                                "Date: %s\nTemperature: %.1f°C, Feels like: %.1f°C\nHumidity: %.1f%%\nDescription: %s\nWind speed: %.1f m/s\n\n",
                                forecast.getDate(),
                                forecast.getTemperature(),
                                forecast.getFeelsLike(),
                                forecast.getHumidity(),
                                forecast.getDescription(),
                                forecast.getWindSpeed()
                        ));
                    });
                    
                    CompletableFuture<String> summaryFuture = mcpClient.generateForecastSummary(city, forecastData.toString());
                    return Mono.fromFuture(summaryFuture);
                });
    }
}
