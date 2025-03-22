package br.com.gustavohenrique.MediasAPI.authentication;

import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Autenticar usuário", description = "Através do email e senha do usuário é retornado um token (JWT)" +
            "de acesso para as demais requisições.")
    @PostMapping
    public String authenticate(@RequestBody AuthDto user){
        return authenticationService.authenticate(user);
    }
}
