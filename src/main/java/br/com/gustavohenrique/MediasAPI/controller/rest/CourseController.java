package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.dtos.*;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
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
@RequestMapping("{userId}/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final ModelMapper modelMapper;
    private final CourseService courseService;
    private final ProjectionService projectionService;
    @Autowired
    public CourseController(ModelMapper modelMapper, CourseService courseService, ProjectionService projectionService) {
        this.modelMapper = modelMapper;
        this.courseService = courseService;
        this.projectionService = projectionService;
    }

    @PostMapping
    @Operation(summary = "Criar curso", description = "Cria um novo curso, e através do método de cálculo das médias" +
            " cria automaticamente uma projeção com o mesmo nome, identificando  e instânciando as avaliações definidas.")
    public ResponseEntity<CourseDTO> createCourse(@PathVariable Long userId, @RequestBody @Valid RequestCourseDto course){
           logger.info("Request to create course for user ID: {}", userId);
           courseService.getAuthenticatedUser(userId);
           return ResponseEntity.status(HttpStatus.CREATED)
                   .body(modelMapper.map(courseService.createCourse(userId ,course), CourseDTO.class));
    }

    @GetMapping
    @Operation(summary = "Listar cursos", description = "retorna os cursos pertencentes ao usuário autenticado.")
    public  ResponseEntity<List<CourseDTO>> showCourses(@PathVariable Long userId){
          logger.info("Request to list courses for user ID: {}", userId);
          courseService.getAuthenticatedUser(userId);
          return ResponseEntity.ok(courseService.listCourses(userId).stream()
                  .map(course -> modelMapper.map(course,CourseDTO.class)).collect(Collectors.toList()));
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Alterar nome do curso")
    public ResponseEntity<CourseDTO> updateCourseName(@PathVariable Long userId,@PathVariable Long id,
                                                      @RequestBody @Valid StringRequestDTO nameDto){
            logger.info("Request to update course name for user ID: {} and course ID: {}", userId, id);
            courseService.getAuthenticatedUser(userId);
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseName(userId, id, nameDto),
                    CourseDTO.class));
    }

    @PatchMapping("/{id}/method")
    @Operation(summary = "Alterar método de cálculo", description = "Altera a forma como o método de cálculo da média " +
            "final é definida. Deleta as projeções equivalentes e criar uma nova atualizada")
    public ResponseEntity<CourseDTO> updateCourseMethod(@PathVariable Long userId ,@PathVariable Long id,
                                                        @RequestBody @Valid StringRequestDTO averageMethodDto){
            logger.info("Request to update course method for user ID: {} and course ID: {}", userId, id);
            courseService.getAuthenticatedUser(userId);
            return ResponseEntity.ok(modelMapper.map(courseService
                    .updateCourseAverageMethod(userId, id, averageMethodDto), CourseDTO.class));
    }

    @PatchMapping("/{id}/cutoffgrade")
    @Operation(summary = "Alterar nota de corte")
    public ResponseEntity<CourseDTO> updateCourseCutOffGrade(@PathVariable Long userId ,@PathVariable Long id,
                                                             @RequestBody @Valid DoubleRequestDTO cutOffGradeDto){
            logger.info("Request to update course cut-off grade for user ID: {} and course ID: {}", userId, id);
            courseService.getAuthenticatedUser(userId);
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseCutOffGrade(userId, id, cutOffGradeDto),
                    CourseDTO.class));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable Long userId, @PathVariable Long id){
            logger.info("Request to delete course for user ID: {} and course ID: {}", userId, id);
            courseService.getAuthenticatedUser(userId);
            return ResponseEntity.ok(modelMapper.map(courseService.deleteCourse(userId, id), CourseDTO.class));
    }

    @GetMapping("/projections")
    @Operation(summary = "Listar projeções", description = "Lista todas as projeções de um usuário")
    public ResponseEntity<List<ProjectionDTO>> showAllProjections(@PathVariable Long userId){
        logger.info("Request to list all projections for user ID: {}", userId);
        courseService.getAuthenticatedUser(userId);
        return ResponseEntity.ok(projectionService.listAllProjection(userId).stream().map(projection ->
                modelMapper.map(projection,ProjectionDTO.class)).collect(Collectors.toList()));
    }

}
