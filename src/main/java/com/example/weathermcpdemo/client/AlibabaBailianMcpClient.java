package com.example.weathermcpdemo.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Client implementation for AI-powered weather analysis
 */
@Component
public class AlibabaBailianMcpClient {

    private final ChatClient chatClient;

    @Autowired
    public AlibabaBailianMcpClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Send a query to the AI model
     *
     * @param prompt The prompt to send to the AI model
     * @return CompletableFuture with the response from the AI model
     */
    public CompletableFuture<String> query(String prompt) {
        return CompletableFuture.supplyAsync(() -> {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        });
    }

    /**
     * Generate a weather analysis based on weather data
     *
     * @param weatherData The weather data to analyze
     * @return CompletableFuture with the analysis from the AI model
     */
    public CompletableFuture<String> generateWeatherAnalysis(String weatherData) {
        String prompt = "Based on the following weather data, provide a detailed analysis and recommendations:\n\n" + weatherData;
        return query(prompt);
    }

    /**
     * Generate a weather forecast summary
     *
     * @param city The city name
     * @param forecastData The forecast data
     * @return CompletableFuture with the summary from the AI model
     */
    public CompletableFuture<String> generateForecastSummary(String city, String forecastData) {
        String prompt = "Summarize the weather forecast for " + city + " based on the following data:\n\n" + forecastData;
        return query(prompt);
    }
}
