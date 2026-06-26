package br.com.gustavohenrique.MediasAPI.dtos;

public record TokenResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        String refreshToken
) {}
