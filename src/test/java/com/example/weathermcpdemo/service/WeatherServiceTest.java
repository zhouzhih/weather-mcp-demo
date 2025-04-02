package com.example.weathermcpdemo.service;

import com.example.weathermcpdemo.client.AlibabaBailianMcpClient;
import com.example.weathermcpdemo.client.WeatherClient;
import com.example.weathermcpdemo.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class WeatherServiceTest {

    @MockBean
    private WeatherClient weatherClient;

    @MockBean
    private AlibabaBailianMcpClient mcpClient;

    private WeatherService weatherService;

    @BeforeEach
    public void setup() {
        weatherService = new WeatherService(weatherClient, mcpClient);
    }

    @Test
    public void testGetCurrentWeather() {
        String city = "Beijing";
        WeatherResponse mockResponse = createMockWeatherResponse(city);
        when(weatherClient.getCurrentWeather(anyString())).thenReturn(Mono.just(mockResponse));

        Mono<WeatherResponse> weatherResponseMono = weatherService.getCurrentWeather(city);

        StepVerifier.create(weatherResponseMono)
                .assertNext(response -> {
                    System.out.println("Weather Response: " + response);
                    assert response != null;
                    assert response.getCity().equals(city);
                    assert response.getCurrent() != null;
                })
                .verifyComplete();
    }

    @Test
    public void testGetWeatherAnalysis() {
        String city = "Shanghai";
        WeatherResponse mockResponse = createMockWeatherResponse(city);
        String mockAnalysis = "Today in Shanghai: Sunny with a high of 25°C";
        
        when(weatherClient.getCurrentWeather(anyString())).thenReturn(Mono.just(mockResponse));
        when(mcpClient.generateWeatherAnalysis(anyString())).thenReturn(CompletableFuture.completedFuture(mockAnalysis));

        Mono<String> analysisMono = weatherService.getWeatherAnalysis(city);

        StepVerifier.create(analysisMono)
                .assertNext(analysis -> {
                    System.out.println("Weather Analysis: " + analysis);
                    assert analysis != null;
                    assert analysis.equals(mockAnalysis);
                })
                .verifyComplete();
    }
    
    private WeatherResponse createMockWeatherResponse(String city) {
        WeatherResponse.WeatherData weatherData = new WeatherResponse.WeatherData();
        weatherData.setTemperature(25.0);
        weatherData.setFeelsLike(26.0);
        weatherData.setHumidity(60.0);
        weatherData.setDescription("Sunny");
        weatherData.setWindSpeed(5.0);
        weatherData.setDate("2025-03-27");
        
        WeatherResponse response = new WeatherResponse();
        response.setCity(city);
        response.setCountry("China");
        response.setCurrent(weatherData);
        response.setForecast(new ArrayList<>());
        
        return response;
    }
}
