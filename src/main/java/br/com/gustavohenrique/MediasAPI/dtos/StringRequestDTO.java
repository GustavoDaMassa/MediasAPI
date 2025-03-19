package br.com.gustavohenrique.MediasAPI.dtos;

import jakarta.validation.constraints.NotBlank;

public record StringRequestDTO(
       @NotBlank String string
) {
}
