package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.MapperDTOs;
import br.com.gustavohenrique.MediasAPI.model.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.ProjectionService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{courseId}/projections")
public class ProjectionController {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MapperDTOs mapperDTOs;


    private final ProjectionService projectionService;

    public ProjectionController(ProjectionService projectionService) {
        this.projectionService = projectionService;
    }

    @PostMapping
    public ResponseEntity<ProjectionDTO> createProjection(@PathVariable Long courseId, @RequestBody @Valid StringRequestDTO projectionName){
            return ResponseEntity.status(HttpStatus.CREATED).body(mapperDTOs.projectionDTO(projectionService.createProjection(courseId,projectionName)));
    }

    @GetMapping
    public ResponseEntity<List<ProjectionDTO>> showProjections(@PathVariable Long courseId){
            return ResponseEntity.ok(projectionService.listProjection(courseId).stream().map(projection -> mapperDTOs.projectionDTO(projection)).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectionDTO> updateProjectionName(@PathVariable Long courseId, @PathVariable Long id, @RequestBody StringRequestDTO newProjectNameDto){
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(projectionService.updateProjectionName(courseId, id, newProjectNameDto),ProjectionDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProjectionDTO> deleteProjection(@PathVariable Long courseId, @PathVariable Long id){
            return ResponseEntity.ok(modelMapper.map(projectionService.deleteProjection(courseId,id),ProjectionDTO.class));
    }

    @DeleteMapping("/all")
    public void deleteAllProjections(@PathVariable Long courseId){
        projectionService.deleteAllProjections(courseId);
    }
}
