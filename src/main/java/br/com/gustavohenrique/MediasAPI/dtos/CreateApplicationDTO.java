package br.com.gustavohenrique.MediasAPI.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateApplicationDTO(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 500) String description
) {}
