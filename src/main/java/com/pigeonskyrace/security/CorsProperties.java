package com.pigeonskyrace.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(String allowedOrigins) {

    public List<String> origins() {
        if (allowedOrigins == null || allowedOrigins.isBlank()) {
            return List.of("http://localhost:*", "http://127.0.0.1:*");
        }
        return Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
