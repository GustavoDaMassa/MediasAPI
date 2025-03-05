package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.dtos.DoubleRequestDTO;

import java.util.List;

public interface AssessmentService {

    void createAssessment(Long projectionId);

    List<Assessment> listAssessment(Long projectionId);

    Assessment insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto);
}
