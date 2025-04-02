package com.example.weathermcpdemo.controller;

import com.example.weathermcpdemo.client.AlibabaBailianMcpClient;
import com.example.weathermcpdemo.host.AlibabaBailianMcpHost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Controller for testing AI components
 */
@RestController
@RequestMapping("/api/test")
@Slf4j
public class TestController {

    private final AlibabaBailianMcpClient aiClient;
    private final AlibabaBailianMcpHost aiHost;

    @Autowired
    public TestController(AlibabaBailianMcpClient aiClient, AlibabaBailianMcpHost aiHost) {
        this.aiClient = aiClient;
        this.aiHost = aiHost;
    }

    /**
     * Test AI client
     *
     * @param prompt The prompt to send to the AI model
     * @return Response from the AI client
     */
    @GetMapping("/client")
    public Mono<ResponseEntity<String>> testAiClient(@RequestParam String prompt) {
        log.info("Testing AI client with prompt: {}", prompt);
        CompletableFuture<String> future = aiClient.query(prompt);
        return Mono.fromFuture(future)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(error -> log.error("Error testing AI client: {}", error.getMessage()));
    }

    /**
     * Test AI host
     *
     * @param prompt The prompt to send to the AI model
     * @return Response from the AI host
     */
    @GetMapping("/host")
    public Mono<ResponseEntity<String>> testAiHost(@RequestParam String prompt) {
        log.info("Testing AI host with prompt: {}", prompt);
        return aiHost.callAiModel(prompt)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(error -> log.error("Error testing AI host: {}", error.getMessage()));
    }
}
