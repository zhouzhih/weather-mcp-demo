package com.example.weathermcpdemo.client;

import com.example.weathermcpdemo.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OpenWeatherMapClient implements WeatherClient {

    private final WebClient webClient;
    private final String apiKey;

    public OpenWeatherMapClient(
            @Value("${weather.api.url}") String apiUrl,
            @Value("${weather.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .build();
        this.apiKey = apiKey;
    }

    @Override
    public Mono<WeatherResponse> getCurrentWeather(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::mapToWeatherResponse);
    }

    @Override
    public Mono<WeatherResponse> getWeatherForecast(String city, int days) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .queryParam("cnt", Math.min(days * 8, 40)) // API limits to 5 days / 3 hour forecast
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> mapToForecastResponse(response, city));
    }

    private WeatherResponse mapToWeatherResponse(Map<String, Object> response) {
        Map<String, Object> main = (Map<String, Object>) response.get("main");
        List<Map<String, Object>> weather = (List<Map<String, Object>>) response.get("weather");
        Map<String, Object> wind = (Map<String, Object>) response.get("wind");
        Map<String, Object> sys = (Map<String, Object>) response.get("sys");
        
        String description = weather != null && !weather.isEmpty() ? (String) weather.get(0).get("description") : "";
        String icon = weather != null && !weather.isEmpty() ? (String) weather.get(0).get("icon") : "";
        
        WeatherResponse.WeatherData currentData = WeatherResponse.WeatherData.builder()
                .temperature((Double) main.get("temp"))
                .feelsLike((Double) main.get("feels_like"))
                .humidity((Double) main.get("humidity"))
                .description(description)
                .windSpeed((Double) wind.get("speed"))
                .iconUrl("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                .build();
        
        return WeatherResponse.builder()
                .city((String) response.get("name"))
                .country((String) sys.get("country"))
                .current(currentData)
                .build();
    }
    
    private WeatherResponse mapToForecastResponse(Map<String, Object> response, String city) {
        List<Map<String, Object>> forecastList = (List<Map<String, Object>>) response.get("list");
        Map<String, Object> cityInfo = (Map<String, Object>) response.get("city");
        
        List<WeatherResponse.WeatherData> forecast = new ArrayList<>();
        
        for (Map<String, Object> item : forecastList) {
            Map<String, Object> main = (Map<String, Object>) item.get("main");
            List<Map<String, Object>> weather = (List<Map<String, Object>>) item.get("weather");
            Map<String, Object> wind = (Map<String, Object>) item.get("wind");
            
            String description = weather != null && !weather.isEmpty() ? (String) weather.get(0).get("description") : "";
            String icon = weather != null && !weather.isEmpty() ? (String) weather.get(0).get("icon") : "";
            
            WeatherResponse.WeatherData data = WeatherResponse.WeatherData.builder()
                    .date((String) item.get("dt_txt"))
                    .temperature((Double) main.get("temp"))
                    .feelsLike((Double) main.get("feels_like"))
                    .humidity((Double) main.get("humidity"))
                    .description(description)
                    .windSpeed((Double) wind.get("speed"))
                    .iconUrl("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                    .build();
            
            forecast.add(data);
        }
        
        return WeatherResponse.builder()
                .city(city)
                .country((String) cityInfo.get("country"))
                .forecast(forecast)
                .build();
    }
}
