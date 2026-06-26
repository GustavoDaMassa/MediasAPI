package br.com.gustavohenrique.MediasAPI.controller.rest.v1;

import br.com.gustavohenrique.MediasAPI.dtos.*;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
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
@RequestMapping("/api/v1/{userId}/courses")
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
    @Operation(summary = "Criar curso", description = "Cria um novo curso e automaticamente uma projeção com as avaliações definidas pelo método de cálculo.")
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@PathVariable Long userId, @RequestBody @Valid RequestCourseDto course) {
        logger.info("Request to create course for user ID: {}", userId);
        courseService.validateOwnership(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(modelMapper.map(courseService.createCourse(userId, course), CourseDTO.class)));
    }

    @GetMapping
    @Operation(summary = "Listar cursos", description = "Retorna os cursos do usuário autenticado de forma paginada.")
    public ResponseEntity<PageResponse<CourseDTO>> showCourses(@PathVariable Long userId,
                                                               @PageableDefault(size = 20) Pageable pageable) {
        logger.info("Request to list courses for user ID: {}", userId);
        courseService.validateOwnership(userId);
        var page = courseService.listCourses(userId, pageable).map(c -> modelMapper.map(c, CourseDTO.class));
        return ResponseEntity.ok(PageResponse.from(page));
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Alterar nome do curso")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourseName(@PathVariable Long userId, @PathVariable Long id,
                                                                   @RequestBody @Valid StringRequestDTO nameDto) {
        logger.info("Request to update course name for user ID: {} and course ID: {}", userId, id);
        courseService.validateOwnership(userId);
        return ResponseEntity.ok(ApiResponse.of(modelMapper.map(courseService.updateCourseName(userId, id, nameDto), CourseDTO.class)));
    }

    @PatchMapping("/{id}/method")
    @Operation(summary = "Alterar método de cálculo", description = "Altera a fórmula de cálculo da média final. Recria projeção e avaliações.")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourseMethod(@PathVariable Long userId, @PathVariable Long id,
                                                                     @RequestBody @Valid StringRequestDTO averageMethodDto) {
        logger.info("Request to update course method for user ID: {} and course ID: {}", userId, id);
        courseService.validateOwnership(userId);
        return ResponseEntity.ok(ApiResponse.of(modelMapper.map(courseService.updateCourseAverageMethod(userId, id, averageMethodDto), CourseDTO.class)));
    }

    @PatchMapping("/{id}/cutoffgrade")
    @Operation(summary = "Alterar nota de corte")
    public ResponseEntity<ApiResponse<CourseDTO>> updateCourseCutOffGrade(@PathVariable Long userId, @PathVariable Long id,
                                                                          @RequestBody @Valid DoubleRequestDTO cutOffGradeDto) {
        logger.info("Request to update course cut-off grade for user ID: {} and course ID: {}", userId, id);
        courseService.validateOwnership(userId);
        return ResponseEntity.ok(ApiResponse.of(modelMapper.map(courseService.updateCourseCutOffGrade(userId, id, cutOffGradeDto), CourseDTO.class)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso")
    public ResponseEntity<ApiResponse<CourseDTO>> deleteCourse(@PathVariable Long userId, @PathVariable Long id) {
        logger.info("Request to delete course for user ID: {} and course ID: {}", userId, id);
        courseService.validateOwnership(userId);
        return ResponseEntity.ok(ApiResponse.of(modelMapper.map(courseService.deleteCourse(userId, id), CourseDTO.class)));
    }

    @GetMapping("/projections")
    @Operation(summary = "Listar todas as projeções do usuário", description = "Lista todas as projeções de todos os cursos do usuário, paginadas.")
    public ResponseEntity<PageResponse<ProjectionDTO>> showAllProjections(@PathVariable Long userId,
                                                                          @PageableDefault(size = 20) Pageable pageable) {
        logger.info("Request to list all projections for user ID: {}", userId);
        courseService.validateOwnership(userId);
        var page = projectionService.listAllProjection(userId, pageable).map(p -> modelMapper.map(p, ProjectionDTO.class));
        return ResponseEntity.ok(PageResponse.from(page));
    }

}
