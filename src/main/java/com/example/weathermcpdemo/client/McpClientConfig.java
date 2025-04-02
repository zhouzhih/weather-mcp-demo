package com.example.weathermcpdemo.client;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpClientConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Bean
    public ChatClient chatClient() {
        OpenAiApi openAiApi = new OpenAiApi(apiKey);
        OpenAiChatModel chatModel = new OpenAiChatModel(openAiApi);
        
        return ChatClient.builder(chatModel)
                .build();
    }
}
