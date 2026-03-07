package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateFinalGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import br.com.gustavohenrique.MediasAPI.service.RpnEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculateFinalGradeImpl implements ICalculateFinalGrade {

    private final ProjectionRepository projectionRepository;
    private final AssessmentRepository assessmentRepository;
    private final IConvertToPolishNotation convertToPolishNotation;
    private final RpnEvaluator rpnEvaluator;

    @Autowired
    public CalculateFinalGradeImpl(ProjectionRepository projectionRepository,
                                   AssessmentRepository assessmentRepository,
                                   IConvertToPolishNotation convertToPolishNotation,
                                   RpnEvaluator rpnEvaluator) {
        this.projectionRepository = projectionRepository;
        this.assessmentRepository = assessmentRepository;
        this.convertToPolishNotation = convertToPolishNotation;
        this.rpnEvaluator = rpnEvaluator;
    }

    @Override
    @Transactional
    public void calculateResult(Projection projection, String averageMethod) {
        var tokens = convertToPolishNotation.convertToPolishNotation(averageMethod);
        double grade = rpnEvaluator.evaluate(tokens, projection.getId(),
                (identifier, projId) ->
                        assessmentRepository.findByIndentifier(identifier, projId).getGrade());
        projection.setFinalGrade(grade);
        projectionRepository.save(projection);
    }
}
