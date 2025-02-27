package br.com.gustavohenrique.MediasAPI.model.dtos;

import jakarta.validation.constraints.NotBlank;

public record StringRequestDTO(
       @NotBlank String string
) {
}
