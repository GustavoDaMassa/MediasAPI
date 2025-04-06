package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Projection;

import java.util.List;

public interface AssessmentService {

    void createAssessment(Projection projectionId);

    List<Assessment> listAssessment(Long projectionId);

    Assessment insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto);
}
