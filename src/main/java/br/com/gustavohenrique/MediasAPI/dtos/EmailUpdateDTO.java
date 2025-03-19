package br.com.gustavohenrique.MediasAPI.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailUpdateDTO(
        @NotBlank
        @Email
        String email
) {
}
