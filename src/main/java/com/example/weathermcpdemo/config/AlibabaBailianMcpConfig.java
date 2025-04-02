package com.example.weathermcpdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for Alibaba Cloud Bailian MCP
 */
@Configuration
public class AlibabaBailianMcpConfig {

    @Value("${alibaba.cloud.bailian.mcp.url}")
    private String mcpUrl;

    @Value("${alibaba.cloud.bailian.mcp.api-key}")
    private String mcpApiKey;

    /**
     * Creates a WebClient for Alibaba Cloud Bailian MCP
     *
     * @return WebClient configured for Alibaba Cloud Bailian MCP
     */
    @Bean
    public WebClient bailianMcpWebClient() {
        return WebClient.builder()
                .baseUrl(mcpUrl)
                .defaultHeader("Authorization", "Bearer " + mcpApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
