package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.controller.rest.mapper.MapDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{courseId}/projections")
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
    @Operation(summary = "Criar projeção", description = "Cria uma nova projeção e automaticamente suas avaliações de " +
            "acordo com a definição do curso.")
    public ResponseEntity<ProjectionDTO> createProjection(@PathVariable Long courseId,
                                                          @RequestBody @Valid StringRequestDTO projectionName){
            logger.info("Request to create projection for course ID: {}", courseId);
            projectionService.getAuthenticatedUserByCourseId(courseId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapDTO.projectionDTO(projectionService.createProjection(courseId,projectionName)));
    }

    @GetMapping
    @Operation(summary = "Listar projeções", description = "retorna todas as projeções de um determinido curso com uma" +
            "lista de avaliações equivalentes.")
    public ResponseEntity<List<ProjectionDTO>> showProjections(@PathVariable Long courseId){
            logger.info("Request to list projections for course ID: {}", courseId);
            projectionService.getAuthenticatedUserByCourseId(courseId);
            return ResponseEntity.ok(projectionService.listProjection(courseId).stream()
                    .map(mapDTO::projectionDTO).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Alterar nome da projeção")
    public ResponseEntity<ProjectionDTO> updateProjectionName(@PathVariable Long courseId, @PathVariable Long id,
                                                              @RequestBody StringRequestDTO newProjectNameDto){
            logger.info("Request to update projection name for course ID: {} and projection ID: {}", courseId, id);
            projectionService.getAuthenticatedUserByCourseId(courseId);
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper
                    .map(projectionService.updateProjectionName(courseId, id, newProjectNameDto),ProjectionDTO.class));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar projeção", description = "Deleta apenas a projeção especificada")
    public ResponseEntity<ProjectionDTO> deleteProjection(@PathVariable Long courseId, @PathVariable Long id){
            logger.info("Request to delete projection for course ID: {} and projection ID: {}", courseId, id);
            projectionService.getAuthenticatedUserByCourseId(courseId);
            return ResponseEntity.ok(modelMapper.map(projectionService.deleteProjection(courseId,id),ProjectionDTO.class));
    }

    @DeleteMapping("/all")
    @Operation(summary = "Deletar todas as projeções", description = "Deleta todas as projeções do curso, incluindo a " +
            "projeção default")
    public void deleteAllProjections(@PathVariable Long courseId){
        logger.info("Request to delete all projections for course ID: {}", courseId);
        projectionService.getAuthenticatedUserByCourseId(courseId);
        var user = userService.getAuthenticatedUser();
        projectionService.deleteAllProjections(courseId, user.getId());
    }
}