package br.com.gustavohenrique.MediasAPI.dtos;

import jakarta.validation.constraints.NotBlank;

public record LogOnDto(
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
