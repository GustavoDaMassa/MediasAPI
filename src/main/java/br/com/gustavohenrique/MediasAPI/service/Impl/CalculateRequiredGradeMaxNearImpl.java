package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.service.FormulaTokens;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateRequiredGradeMaxNear;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ISimulationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CalculateRequiredGradeMaxNearImpl implements ICalculateRequiredGradeMaxNear {

    private final AssessmentRepository assessmentRepository;
    private final IConvertToPolishNotation convertToPolishNotation;
    private final ISimulationResult simulationResult;

    @Autowired
    public CalculateRequiredGradeMaxNearImpl(AssessmentRepository assessmentRepository,
                                             IConvertToPolishNotation convertToPolishNotation,
                                             ISimulationResult simulationResult) {
        this.assessmentRepository = assessmentRepository;
        this.convertToPolishNotation = convertToPolishNotation;
        this.simulationResult = simulationResult;
    }

    @Override
    @Transactional
    public void calculateRequiredGradeMaxNear(Projection projection, Course course) {
        var polishNotation = convertToPolishNotation.convertToPolishNotation(course.getAverageMethod());
        double cutOffGrade = course.getCutOffGrade();

        double maxPossible = simulationResult.simulate(Double.MAX_VALUE, projection, polishNotation);
        if (maxPossible < cutOffGrade) {
            for (String token : polishNotation) {
                if (FormulaTokens.isIdentifier(token)) {
                    var assessment = assessmentRepository.findByIndentifier(
                            FormulaTokens.cleanBrackets(token), projection.getId());
                    assessment.setRequiredGradeMaxNear(assessment.isFixed() ? 0.0 : -1.0);
                    assessmentRepository.save(assessment);
                }
            }
            return;
        }

        List<String> orderedIdentifiers = orderedUniqueIdentifiers(polishNotation);
        double biggerMaxValue = assessmentRepository.getBiggerMaxValue(projection.getId());

        Set<String> maxedIdentifiers = new LinkedHashSet<>();

        for (String identifier : orderedIdentifiers) {
            var assessment = assessmentRepository.findByIndentifier(identifier, projection.getId());

            if (assessment.isFixed()) {
                assessment.setRequiredGradeMaxNear(0.0);
                assessmentRepository.save(assessment);
                maxedIdentifiers.add(identifier);
                continue;
            }

            double requiredGrade = binarySearch(projection, polishNotation, cutOffGrade,
                    biggerMaxValue, maxedIdentifiers);

            assessment.setRequiredGradeMaxNear(Math.min(requiredGrade, assessment.getMaxValue()));
            assessmentRepository.save(assessment);

            maxedIdentifiers.add(identifier);
        }
    }

    private List<String> orderedUniqueIdentifiers(ArrayList<String> polishNotation) {
        List<String> ordered = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        for (String token : polishNotation) {
            if (FormulaTokens.isIdentifier(token)) {
                String clean = FormulaTokens.cleanBrackets(token);
                if (seen.add(clean)) {
                    ordered.add(clean);
                }
            }
        }
        return ordered;
    }

    private double binarySearch(Projection projection, ArrayList<String> polishNotation,
                                double cutOffGrade, double biggerMaxValue,
                                Set<String> maxedIdentifiers) {
        double requiredGrade = 0;
        double result = 0;
        int i;
        for (i = 0; i <= biggerMaxValue * 100; i += 100) {
            requiredGrade = (double) i / 100;
            result = simulationResult.simulateWithMaxed(requiredGrade, projection, polishNotation, maxedIdentifiers);
            if (result >= cutOffGrade) break;
        }
        if (result > cutOffGrade) {
            int j;
            for (j = i - 10; j >= i - 100; j -= 10) {
                if (j <= 0) {
                    requiredGrade = 0;
                    break;
                }
                requiredGrade = (double) j / 100;
                result = simulationResult.simulateWithMaxed(requiredGrade, projection, polishNotation, maxedIdentifiers);
                if (result <= cutOffGrade) break;
            }
            if (result < cutOffGrade) {
                for (int k = j + 10; k <= j + 10; k++) {
                    requiredGrade = (double) k / 100;
                    result = simulationResult.simulateWithMaxed(requiredGrade, projection, polishNotation, maxedIdentifiers);
                    if (result >= cutOffGrade) break;
                }
            }
        }
        return requiredGrade;
    }
}
