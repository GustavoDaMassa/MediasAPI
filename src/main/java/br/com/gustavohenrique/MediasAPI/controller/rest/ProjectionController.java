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

import java.util.stream.Collectors;

@RestController
@RequestMapping("{userId}/courses/{courseId}/projections")
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
    public ResponseEntity<?> createProjection(@PathVariable Long userId, @PathVariable Long courseId, @RequestBody @Valid StringRequestDTO projectionName){

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mapperDTOs.projectionDTO(projectionService.createProjection(userId,courseId,projectionName)));
        } catch (IllegalArgumentException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping
    public ResponseEntity<?> showProjections(@PathVariable Long userId, @PathVariable Long courseId){

        try {
            return ResponseEntity.ok(projectionService.listProjection(userId,courseId).stream().map(projection -> mapperDTOs.projectionDTO(projection)).collect(Collectors.toList()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> updateProjectionName(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long id, @RequestBody StringRequestDTO newProjectNameDto){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(projectionService.updateProjectionName(userId, courseId, id, newProjectNameDto),ProjectionDTO.class));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProjection(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long id){
        try {
            return ResponseEntity.ok(modelMapper.map(projectionService.deleteProjection(userId,courseId,id),ProjectionDTO.class));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/all")
    public void deleteAllProjections(@PathVariable Long userId, @PathVariable Long courseId){
        projectionService.deleteAllProjections(userId,courseId);
    }
}
