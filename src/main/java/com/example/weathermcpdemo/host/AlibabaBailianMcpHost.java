package com.example.weathermcpdemo.host;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Host component for AI-powered weather analysis
 */
@Component
@Slf4j
public class AlibabaBailianMcpHost {

    private final ChatClient chatClient;

    @Autowired
    public AlibabaBailianMcpHost(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Call AI model with a prompt
     *
     * @param prompt The prompt to send to the AI model
     * @return Response from the AI model
     */
    public Mono<String> callAiModel(String prompt) {
        log.info("Calling AI model with prompt: {}", prompt);
        
        return Mono.fromCallable(() -> {
            String result = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
            log.info("Received response from AI model: {}", result);
            return result;
        });
    }

    /**
     * Get weather analysis from AI model
     *
     * @param weatherData The weather data to analyze
     * @return Analysis from the AI model
     */
    public Mono<String> getWeatherAnalysis(String weatherData) {
        String prompt = "Based on the following weather data, provide a detailed analysis and recommendations:\n\n" + weatherData;
        return callAiModel(prompt);
    }

    /**
     * Get weather forecast summary from AI model
     *
     * @param city The city name
     * @param forecastData The forecast data
     * @return Summary from the AI model
     */
    public Mono<String> getWeatherForecastSummary(String city, String forecastData) {
        String prompt = "Summarize the weather forecast for " + city + " based on the following data:\n\n" + forecastData;
        return callAiModel(prompt);
    }
}
