package br.com.gustavohenrique.MediasAPI.controller.dtos;

import jakarta.validation.constraints.NotBlank;

public record StringRequestDTO(
       @NotBlank String string
) {
}
