package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.exception.AssessmentNotFoundException;
import br.com.gustavohenrique.MediasAPI.exception.ProjectionNotFoundException;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateFinalGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateRequiredGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IIdentifiersDefinition;
import br.com.gustavohenrique.MediasAPI.service.OwnershipValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final ProjectionRepository projectionRepository;
    private final AssessmentRepository assessmentRepository;
    private final IIdentifiersDefinition identifiersDefinition;
    private final ICalculateFinalGrade calculateFinalGrade;
    private final ICalculateRequiredGrade calculateRequiredGrade;
    private final OwnershipValidationService ownershipValidationService;

    @Autowired
    public AssessmentServiceImpl(ProjectionRepository projectionRepository, AssessmentRepository assessmentRepository,
                                 IIdentifiersDefinition identifiersDefinition, ICalculateFinalGrade calculateFinalGrade,
                                 ICalculateRequiredGrade calculateRequiredGrade,
                                 OwnershipValidationService ownershipValidationService) {
        this.projectionRepository = projectionRepository;
        this.assessmentRepository = assessmentRepository;
        this.identifiersDefinition = identifiersDefinition;
        this.calculateFinalGrade = calculateFinalGrade;
        this.calculateRequiredGrade = calculateRequiredGrade;
        this.ownershipValidationService = ownershipValidationService;
    }

    @Transactional
    public void createAssessment(Projection projection){
        var course = projection.getCourse();
        identifiersDefinition.defineIdentifiers(course.getAverageMethod(),projection);
        calculateFinalGrade.calculateResult(projection,course.getAverageMethod());
        calculateRequiredGrade.calculateRequiredGrade(projection,course);
    }

    public List<Assessment> listAssessment(Long projectionId) {
        validateProjection(projectionId);
        var projection = projectionRepository.findById(projectionId).orElseThrow();
        return assessmentRepository.findByProjection(projection);
    }

    @Transactional
    public Assessment insertGrade(Long projectionId, Long id, DoubleRequestDTO gradeDto){
        validateProjection(projectionId);
        var projection = projectionRepository.findById(projectionId).orElseThrow();
        var assessment = assessmentRepository.findByProjectionIdAndId(projectionId,id).orElseThrow(() ->
                new AssessmentNotFoundException(id, projectionId));
        var course = assessment.getProjection().getCourse();

        assessment.applyGrade(gradeDto.value());
        calculateFinalGrade.calculateResult(projection,course.getAverageMethod());
        calculateRequiredGrade.calculateRequiredGrade(projection,course);
        return assessment;
    }

    public void validateProjection(Long projectionId){
        if(!projectionRepository.existsById(projectionId))
            throw new ProjectionNotFoundException(projectionId);
    }

    @Override
    public void validateOwnership(Long projectionId) {
        var projection = projectionRepository.findById(projectionId)
                .orElseThrow(() -> new ProjectionNotFoundException(projectionId));
        ownershipValidationService.validate(projection.getCourse().getUser().getId());
    }
}
