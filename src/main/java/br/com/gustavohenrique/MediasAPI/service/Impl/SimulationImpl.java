package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ISimulationResult;
import br.com.gustavohenrique.MediasAPI.service.RpnEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class SimulationImpl implements ISimulationResult {

    private final AssessmentRepository assessmentRepository;
    private final RpnEvaluator rpnEvaluator;

    @Autowired
    public SimulationImpl(AssessmentRepository assessmentRepository, RpnEvaluator rpnEvaluator) {
        this.assessmentRepository = assessmentRepository;
        this.rpnEvaluator = rpnEvaluator;
    }

    @Override
    public double simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation) {
        return rpnEvaluator.evaluate(polishNotation, projection.getId(),
                (identifier, projId) -> {
                    var assessment = assessmentRepository.findByIndentifier(identifier, projId);
                    return assessment.isFixed()
                            ? assessment.getGrade()
                            : Math.min(requiredGrade, assessment.getMaxValue());
                });
    }

    @Override
    public double simulateWithMaxed(double requiredGrade, Projection projection,
                                    ArrayList<String> polishNotation,
                                    Set<String> maxedIdentifiers) {
        return rpnEvaluator.evaluate(polishNotation, projection.getId(),
                (identifier, projId) -> {
                    var assessment = assessmentRepository.findByIndentifier(identifier, projId);
                    if (assessment.isFixed()) return assessment.getGrade();
                    if (maxedIdentifiers.contains(identifier)) return assessment.getMaxValue();
                    return Math.min(requiredGrade, assessment.getMaxValue());
                });
    }
}
