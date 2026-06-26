package br.com.gustavohenrique.MediasAPI.dtos;

import java.time.Instant;

public record ApplicationDTO(
        Long id,
        String name,
        String description,
        String apiKeyPrefix,
        boolean active,
        int rateLimitPerMinute,
        Instant createdAt
) {}
