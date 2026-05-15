package com.seanergy.oa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClaudeConfig {

    @Value("${claude.api-key}")
    private String apiKey;

    @Value("${claude.model:claude-sonnet-4-6-20250514}")
    private String model;

    @Bean
    public WebClient claudeWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.anthropic.com/v1")
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
