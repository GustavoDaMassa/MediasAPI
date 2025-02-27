package br.com.gustavohenrique.MediasAPI.controller.rest;


import br.com.gustavohenrique.MediasAPI.model.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.service.AssessmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("{userId}/courses/{courseId}/projections/{projectionId}/assessment")
public class AssessmentController {

    @Autowired
    private ModelMapper modelMapper;

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> insertGrade(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long projectionId, @PathVariable Long id, @RequestBody DoubleRequestDTO gradeDto){
        try {
            return ResponseEntity.ok(modelMapper.map(assessmentService.insertGrade(userId,courseId,projectionId,id,gradeDto), AssessmentDTO.class));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> showAssessment(@PathVariable Long userId, @PathVariable Long courseId, @PathVariable Long projectionId){
        try {
            return ResponseEntity.ok(assessmentService.listAssessment(userId,courseId,projectionId).stream().map(assessment -> modelMapper.map(assessment, AssessmentDTO.class)).collect(Collectors.toList()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
