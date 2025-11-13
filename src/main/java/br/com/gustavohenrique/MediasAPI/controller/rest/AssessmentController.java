package br.com.gustavohenrique.MediasAPI.controller.rest;


import br.com.gustavohenrique.MediasAPI.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{projectionId}/assessment")
public class AssessmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentController.class);
    private final ModelMapper modelMapper;
    private final AssessmentService assessmentService;
    @Autowired
    public AssessmentController(ModelMapper modelMapper, AssessmentService assessmentService) {
        this.modelMapper = modelMapper;
        this.assessmentService = assessmentService;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Inserir nota",description = "Posta a nota adquirida. Automaticamente a média final é " +
            "calculada juntamente com o quanto de pontuação ainda falta em cada avaliação ainda não realizada," +
            " para atingir a nota de corte")
    public ResponseEntity<AssessmentDTO> insertGrade(@PathVariable Long projectionId, @PathVariable Long id,
                                                     @RequestBody DoubleRequestDTO gradeDto){
            logger.info("Request to insert grade for projection ID: {} and assessment ID: {}", projectionId, id);
            assessmentService.getAuthenticatedUserByProjectionId(projectionId);
            return ResponseEntity.ok(modelMapper.map(assessmentService.insertGrade(projectionId,id,gradeDto),
                    AssessmentDTO.class));
    }

    @GetMapping
    @Operation(summary = "Listar avaliações", description = "Lista todas as avaliações de uma projeção")
    public ResponseEntity<List<AssessmentDTO>> showAssessment(@PathVariable Long projectionId){
            logger.info("Request to list assessments for projection ID: {}", projectionId);
            assessmentService.getAuthenticatedUserByProjectionId(projectionId);
            return ResponseEntity.ok(assessmentService.listAssessment(projectionId).stream().map(assessment
                    -> modelMapper.map(assessment, AssessmentDTO.class)).collect(Collectors.toList()));
    }
}
