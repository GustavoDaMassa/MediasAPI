package br.com.gustavohenrique.MediasAPI.model.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserDTO {

    @NotBlank
    String name;

    @NotBlank
    @Email
    @Column(unique = true)
    String email;
}
