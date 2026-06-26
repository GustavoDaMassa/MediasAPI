package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssessmentService extends OwnershipValidator {

    void createAssessment(Projection projectionId);

    Page<Assessment> listAssessment(Long projectionId, Pageable pageable);

    Assessment insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto);

    void resetAll(Long projectionId);

}
