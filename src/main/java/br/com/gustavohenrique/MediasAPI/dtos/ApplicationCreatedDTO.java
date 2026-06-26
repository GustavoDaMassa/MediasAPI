package br.com.gustavohenrique.MediasAPI.dtos;

import java.time.Instant;

public record ApplicationCreatedDTO(
        Long id,
        String name,
        String description,
        String apiKey,
        String apiKeyPrefix,
        boolean active,
        int rateLimitPerMinute,
        Instant createdAt
) {}
