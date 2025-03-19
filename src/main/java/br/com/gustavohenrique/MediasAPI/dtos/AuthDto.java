package br.com.gustavohenrique.MediasAPI.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter @Getter
@NoArgsConstructor
public class AuthDto {
    private String email;
    private String password;
}
