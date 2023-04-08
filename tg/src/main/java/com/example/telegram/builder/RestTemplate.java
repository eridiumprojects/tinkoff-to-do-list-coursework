package com.example.telegram.builder;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

public class RestTemplate {
    @Bean
    public org.springframework.web.client.RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
