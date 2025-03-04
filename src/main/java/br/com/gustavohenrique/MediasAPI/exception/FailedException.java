package br.com.gustavohenrique.MediasAPI.exception;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


public class FailedException extends  RuntimeException{

    private final AssessmentRepository assessmentRepository;

    public FailedException(String message, ArrayList<String> polishNotation, Long projectionId
            ,AssessmentRepository assessmentRepository) {
        super(message);
        this.assessmentRepository = assessmentRepository;

        for (int j = 0; j < polishNotation.size(); j++) {
            if(polishNotation.get(j).matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")) {
                var assessment = assessmentRepository.findByIndentifier(polishNotation.get(j)
                        .replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""), projectionId);
                if (!assessment.isFixed())assessment.setRequiredGrade(assessment.getMaxValue());
                else assessment.setRequiredGrade(0.00);
                assessmentRepository.save(assessment);
            }
        }
    }
}
