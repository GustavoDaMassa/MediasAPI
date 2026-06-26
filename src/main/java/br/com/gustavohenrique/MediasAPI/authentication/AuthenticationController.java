package br.com.gustavohenrique.MediasAPI.authentication;

import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import br.com.gustavohenrique.MediasAPI.dtos.RefreshRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
@Tag(name = "Authentication", description = "Endpoints de autenticação e gerenciamento de tokens")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica via email e senha. Retorna access token JWT (15 min) e refresh token (7 dias)."
    )
    @PostMapping
    public ResponseEntity<TokenResponseDTO> authenticate(@RequestBody AuthDto user) {
        logger.info("Authentication attempt for user: {}", user.getEmail());
        return ResponseEntity.ok(authenticationService.authenticate(user));
    }

    @Operation(
            summary = "Renovar access token",
            description = "Troca um refresh token válido por um novo par access token + refresh token (token rotation)."
    )
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody @Valid RefreshRequestDTO request) {
        logger.info("Token refresh requested");
        return ResponseEntity.ok(authenticationService.refresh(request));
    }

    @Operation(
            summary = "Logout",
            description = "Revoga o refresh token. O access token expira naturalmente em até 15 min."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequestDTO request) {
        logger.info("Logout requested");
        authenticationService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
