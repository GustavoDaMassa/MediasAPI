package br.com.gustavohenrique.MediasAPI.controller.rest;


import br.com.gustavohenrique.MediasAPI.model.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.AssessmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{projectionId}/assessment")
public class AssessmentController {

    @Autowired
    private ModelMapper modelMapper;

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AssessmentDTO> insertGrade(@PathVariable Long projectionId, @PathVariable Long id, @RequestBody DoubleRequestDTO gradeDto){
            return ResponseEntity.ok(modelMapper.map(assessmentService.insertGrade(projectionId,id,gradeDto), AssessmentDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDTO>> showAssessment(@PathVariable Long projectionId){
            return ResponseEntity.ok(assessmentService.listAssessment(projectionId).stream().map(assessment -> modelMapper.map(assessment, AssessmentDTO.class)).collect(Collectors.toList()));
    }
}
