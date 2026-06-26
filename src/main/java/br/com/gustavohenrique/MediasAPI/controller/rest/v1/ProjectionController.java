package br.com.gustavohenrique.MediasAPI.controller.rest.v1;

import br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper.MapDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ApiResponse;
import br.com.gustavohenrique.MediasAPI.dtos.PageResponse;
import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/{courseId}/projections")
public class ProjectionController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectionController.class);
    private final ModelMapper modelMapper;
    private final MapDTO mapDTO;
    private final ProjectionService projectionService;
    private final UserService userService;
    @Autowired
    public ProjectionController(ModelMapper modelMapper, MapDTO mapDTO, ProjectionService projectionService, UserService userService) {
        this.modelMapper = modelMapper;
        this.mapDTO = mapDTO;
        this.projectionService = projectionService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Criar projeção", description = "Cria uma nova projeção e automaticamente suas avaliações de acordo com a definição do curso.")
    public ResponseEntity<ApiResponse<ProjectionDTO>> createProjection(@PathVariable Long courseId,
                                                                       @RequestBody @Valid StringRequestDTO projectionName) {
        logger.info("Request to create projection for course ID: {}", courseId);
        projectionService.validateOwnership(courseId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(mapDTO.projectionDTO(projectionService.createProjection(courseId, projectionName))));
    }

    @GetMapping
    @Operation(summary = "Listar projeções", description = "Retorna as projeções de um curso de forma paginada, com lista de avaliações.")
    public ResponseEntity<PageResponse<ProjectionDTO>> showProjections(@PathVariable Long courseId,
                                                                       @PageableDefault(size = 20) Pageable pageable) {
        logger.info("Request to list projections for course ID: {}", courseId);
        projectionService.validateOwnership(courseId);
        var page = projectionService.listProjection(courseId, pageable).map(mapDTO::projectionDTO);
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Alterar nome da projeção")
    public ResponseEntity<ApiResponse<ProjectionDTO>> updateProjectionName(@PathVariable Long courseId, @PathVariable Long id,
                                                                           @RequestBody StringRequestDTO newProjectNameDto) {
        logger.info("Request to update projection name for course ID: {} and projection ID: {}", courseId, id);
        projectionService.validateOwnership(courseId);
        return ResponseEntity.ok(ApiResponse.of(modelMapper.map(projectionService.updateProjectionName(courseId, id, newProjectNameDto), ProjectionDTO.class)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar projeção", description = "Deleta apenas a projeção especificada")
    public ResponseEntity<ApiResponse<ProjectionDTO>> deleteProjection(@PathVariable Long courseId, @PathVariable Long id) {
        logger.info("Request to delete projection for course ID: {} and projection ID: {}", courseId, id);
        projectionService.validateOwnership(courseId);
        return ResponseEntity.ok(ApiResponse.of(modelMapper.map(projectionService.deleteProjection(courseId, id), ProjectionDTO.class)));
    }

    @PatchMapping("/{id}/reset")
    @Operation(summary = "Resetar projeção", description = "Zera todas as notas lançadas, mantendo a projeção intacta")
    public ResponseEntity<ApiResponse<ProjectionDTO>> resetProjection(@PathVariable Long courseId, @PathVariable Long id) {
        logger.info("Request to reset projection for course ID: {} and projection ID: {}", courseId, id);
        projectionService.validateOwnership(courseId);
        return ResponseEntity.ok(ApiResponse.of(mapDTO.projectionDTO(projectionService.resetProjection(courseId, id))));
    }

    @DeleteMapping("/all")
    @Operation(summary = "Deletar todas as projeções", description = "Deleta todas as projeções do curso, incluindo a projeção default")
    public ResponseEntity<Void> deleteAllProjections(@PathVariable Long courseId) {
        logger.info("Request to delete all projections for course ID: {}", courseId);
        projectionService.validateOwnership(courseId);
        var user = userService.getAuthenticatedUser();
        projectionService.deleteAllProjections(courseId, user.getId());
        return ResponseEntity.ok().build();
    }
}