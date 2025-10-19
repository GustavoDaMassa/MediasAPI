package br.com.gustavohenrique.MediasAPI.service.Impl;


import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateFinalGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateRequiredGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IIdentifiersDefinition;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final ProjectionRepository projectionRepository;
    private final AssessmentRepository assessmentRepository;
    private final IIdentifiersDefinition identifiersDefinition;
    private final ICalculateFinalGrade calculateFinalGrade;
    private final ICalculateRequiredGrade calculateRequiredGrade;
    private final UserService userService;
    @Autowired
    public AssessmentServiceImpl(ProjectionRepository projectionRepository, AssessmentRepository assessmentRepository,
                                 IIdentifiersDefinition identifiersDefinition, ICalculateFinalGrade calculateFinalGrade,
                                 ICalculateRequiredGrade calculateRequiredGrade, UserService userService) {
        this.projectionRepository = projectionRepository;
        this.assessmentRepository = assessmentRepository;
        this.identifiersDefinition = identifiersDefinition;
        this.calculateFinalGrade = calculateFinalGrade;
        this.calculateRequiredGrade = calculateRequiredGrade;
        this.userService = userService;
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
                new NotFoundArgumentException("Assessment id "+id+" not found for the Projection id "+projectionId));
        var course = assessment.getProjection().getCourse();

        if (gradeDto.value() <= assessment.getMaxValue())assessment.setGrade(gradeDto.value());
        else throw new
                IllegalArgumentException("It is not allowed to enter a grade higher than "+assessment.getMaxValue());
        assessment.setFixed(true);
        calculateFinalGrade.calculateResult(projection,course.getAverageMethod());
        calculateRequiredGrade.calculateRequiredGrade(projection,course);
        return assessment;
    }

    public void validateProjection(Long projectionId){
        if(!projectionRepository.existsById(projectionId))
            throw  new NotFoundArgumentException("Projection Id "+projectionId+" not found");
    }

    @Override
    public void getAuthenticatedUserByProjectionId(Long projectionId) {
        var user = userService.getAuthenticatedUser();
        var projection = projectionRepository.findById(projectionId)
                .orElseThrow(() -> new NotFoundArgumentException("Projection id " + projectionId + " not found"));
        if (!projection.getCourse().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
    }
}
