package br.com.gustavohenrique.MediasAPI.controller.rest.v1;

import br.com.gustavohenrique.MediasAPI.dtos.ApiResponse;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationCreatedDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationDTO;
import br.com.gustavohenrique.MediasAPI.dtos.CreateApplicationDTO;
import br.com.gustavohenrique.MediasAPI.model.Application;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Applications", description = "Registro e gestão de aplicações. Criação é pública. Demais operações exigem X-Api-Key.")
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private static final String APP_ATTRIBUTE = "authenticatedApp";

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @Operation(
            summary = "Registrar aplicação",
            description = "Cria uma aplicação e retorna a API key. A chave é exibida apenas uma vez — guarde-a com segurança. Não requer autenticação."
    )
    public ResponseEntity<ApiResponse<ApplicationCreatedDTO>> create(@RequestBody @Valid CreateApplicationDTO dto) {
        logger.info("New application registration: {}", dto.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(applicationService.create(dto)));
    }

    @GetMapping("/me")
    @Operation(summary = "Ver dados da aplicação", description = "Retorna informações da aplicação identificada pelo X-Api-Key.")
    public ResponseEntity<ApiResponse<ApplicationDTO>> getMe(HttpServletRequest request) {
        Application app = (Application) request.getAttribute(APP_ATTRIBUTE);
        return ResponseEntity.ok(ApiResponse.of(applicationService.toDTO(app)));
    }

    @PostMapping("/me/rotate")
    @Operation(
            summary = "Rotacionar chave",
            description = "Gera uma nova API key e invalida a anterior. A nova chave é exibida apenas uma vez."
    )
    public ResponseEntity<ApiResponse<ApplicationCreatedDTO>> rotate(HttpServletRequest request) {
        Application app = (Application) request.getAttribute(APP_ATTRIBUTE);
        logger.info("Key rotation for application ID: {}", app.getId());
        return ResponseEntity.ok(ApiResponse.of(applicationService.rotateKey(app)));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Revogar aplicação", description = "Desativa permanentemente a aplicação. A chave deixa de funcionar imediatamente.")
    public ResponseEntity<Void> revoke(HttpServletRequest request) {
        Application app = (Application) request.getAttribute(APP_ATTRIBUTE);
        logger.info("Revoking application ID: {}", app.getId());
        applicationService.revoke(app);
        return ResponseEntity.noContent().build();
    }
}
