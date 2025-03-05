package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateFinalGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalculateFinalGradeImpl implements ICalculateFinalGrade{

    private final ProjectionRepository projectionRepository;
    private final AssessmentRepository assessmentRepository;
    private final IConvertToPolishNotation convertToPolishNotation;

    @Autowired
    public CalculateFinalGradeImpl(ProjectionRepository projectionRepository, AssessmentRepository assessmentRepository,
                                   IConvertToPolishNotation convertToPolishNotation) {
        this.projectionRepository = projectionRepository;
        this.assessmentRepository = assessmentRepository;
        this.convertToPolishNotation = convertToPolishNotation;
    }

    @Override
    @Transactional
    public void calculateResult(Projection projection, String averageMethod)  {

        ArrayList<String> polishNotation = convertToPolishNotation.convertToPolishNotation(averageMethod);
        Deque<Double> stackDouble = new ArrayDeque<>();
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < polishNotation.size(); i++) {
            String token = polishNotation.get(i);
            if(token.matches("(\\d+(([.,])?\\d+)?)"))
                stackDouble.push(Double.parseDouble(token.replaceAll(",",".")));
            else if(token.matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")){
                stackDouble.push(assessmentRepository.findByIndentifier(token
                        .replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""),projection.getId()).getGrade());
            }
            else {
                if(token.matches("[+*\\-/]")){
                    double b = stackDouble.pop();
                    double a = stackDouble.pop();
                    switch (token){
                        case "+": stackDouble.push(a+b);
                            break;
                        case "-":stackDouble.push(a-b);
                            break;
                        case "*":stackDouble.push(a*b);
                            break;
                        case "/":stackDouble.push(a/b);
                            break;
                    }
                }else{
                    if(token.matches("@M(\\[\\d+]\\()?")){ // @M
                        double result = 0;
                        values.add(stackDouble.pop());

                        Pattern amountMaxValueRegex = Pattern.compile("(?<=\\[)\\d+");
                        Matcher maxValue = amountMaxValueRegex.matcher(token.replaceAll("\\s",""));

                        int maxValueInt = 1;
                        if(maxValue.find())maxValueInt = Integer.parseInt(maxValue.group());

                        if(maxValueInt > values.size()) throw new
                                IllegalArgumentException("It is not possible to select more values than those provided");

                        for (int j = 0; j < maxValueInt; j++) {
                            result += Collections.max(values);
                            values.remove(Collections.max(values));
                        }
                        stackDouble.push(result);
                    }
                    else values.add(stackDouble.pop());
                }
            }
        }
        projection.setFinalGrade(stackDouble.getFirst());
        projectionRepository.save(projection);
    }
}
