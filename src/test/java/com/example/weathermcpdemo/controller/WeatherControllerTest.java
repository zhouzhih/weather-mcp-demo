package com.example.weathermcpdemo.controller;

import com.example.weathermcpdemo.model.WeatherResponse;
import com.example.weathermcpdemo.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testGetCurrentWeather() {
        String city = "Beijing";
        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setCity(city);
        mockResponse.setCountry("China");
        mockResponse.setCurrent(new WeatherResponse.WeatherData());

        when(weatherService.getCurrentWeather(anyString())).thenReturn(Mono.just(mockResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/weather/current").queryParam("city", city).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(WeatherResponse.class)
                .value(response -> {
                    assert response.getCity().equals(city);
                    assert response.getCountry().equals("China");
                });
    }

    @Test
    public void testGetWeatherAnalysis() {
        String city = "Shanghai";
        String mockAnalysis = "Today in Shanghai: Sunny with a high of 25°C";

        when(weatherService.getWeatherAnalysis(anyString())).thenReturn(Mono.just(mockAnalysis));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/weather/analysis").queryParam("city", city).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(mockAnalysis);
    }

    @Test
    public void testGetWeatherForecastSummary() {
        String city = "Guangzhou";
        String mockSummary = "5-day forecast for Guangzhou: Mostly sunny with temperatures ranging from 22-28°C";

        when(weatherService.getWeatherForecastSummary(anyString(), anyInt())).thenReturn(Mono.just(mockSummary));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/weather/forecast/summary")
                        .queryParam("city", city)
                        .queryParam("days", 5)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(mockSummary);
    }
}
