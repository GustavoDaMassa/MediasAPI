package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateFinalGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateRequiredGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ISimulationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalculateRequiredGradeImpl implements ICalculateRequiredGrade {

    private final AssessmentRepository assessmentRepository;
    private final IConvertToPolishNotation convertToPolishNotation;
    private final ISimulationResult simulationResult;
    @Autowired
    public CalculateRequiredGradeImpl(AssessmentRepository assessmentRepository, IConvertToPolishNotation
            convertToPolishNotation, ISimulationResult simulationResult) {
        this.assessmentRepository = assessmentRepository;
        this.convertToPolishNotation = convertToPolishNotation;
        this.simulationResult = simulationResult;
    }

    @Override
    @Transactional
    public void calculateRequiredGrade(Projection projection, Course course){

        var polishNotation = convertToPolishNotation.convertToPolishNotation(course.getAverageMethod());
        double biggerMaxValue = assessmentRepository.getBiggerMaxValue(projection.getId());
        double cutOffGrade = course.getCutOffGrade();
        double requiredGrade = 0;
        double result = 0;
        int i;
        for ( i = 0; i <= biggerMaxValue*100; i+=100) {
            requiredGrade = (double) i/100;
            result = simulationResult.simulate(requiredGrade, projection, polishNotation);
            if (result >= cutOffGrade) break;
        }
        if (result > cutOffGrade) {
            int j;
            for ( j = i-10; j >= i-100; j-=10) {
                if(j<=0){
                    requiredGrade = 0;
                    break;
                }
                requiredGrade = (double) j/100;
                result = simulationResult.simulate(requiredGrade, projection, polishNotation);
                if (result <= cutOffGrade) break;
            }
            if(result<cutOffGrade){
                for (int k = j+10; k <= j+10 ; k++) {
                    requiredGrade = (double) k/100;
                    result = simulationResult.simulate(requiredGrade,projection, polishNotation);
                    if(result>=cutOffGrade)break;
                }
            }
        }
        if(result<cutOffGrade){//------------------ tratar caso em que a nota de corte não vai ser alcançada
            for (int j = 0; j < polishNotation.size(); j++) {
                if(polishNotation.get(j).matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")) {
                    var assessment = assessmentRepository.findByIndentifier(polishNotation.get(j)
                            .replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""), projection.getId());
                    if (!assessment.isFixed())assessment.setRequiredGrade(assessment.getMaxValue());
                    else assessment.setRequiredGrade(0.00);
                    assessmentRepository.save(assessment);
                }
            }
        }
        else {
            for (int j = 0; j < polishNotation.size(); j++) {
                if (polishNotation.get(j).matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")){
                    var assessment = assessmentRepository.findByIndentifier(polishNotation.get(j)
                            .replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""),projection.getId());
                    if (!assessment.isFixed())
                        assessment.setRequiredGrade(Double.min(requiredGrade,assessment.getMaxValue()));
                    else assessment.setRequiredGrade(0.00);
                    assessmentRepository.save(assessment);
                }
            }
        }
    }
}
