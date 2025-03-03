package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.exception.FailedException;
import br.com.gustavohenrique.MediasAPI.model.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AssessmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private  final ProjectionRepository projectionRepository;
    private final AssessmentRepository assessmentRepository;

    public AssessmentService(UserRepository userRepository, CourseRepository courseRepository, ProjectionRepository projectionRepository, AssessmentRepository assessmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.projectionRepository = projectionRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public void createAssessment(Long courseId, Long projectionId, String averageMethod){
        var course = courseRepository.findById(courseId).orElseThrow();
        var projection = projectionRepository.findByCourseAndId(course, projectionId).orElseThrow();
        ArrayList<String> methodTokens =  compileRegex(averageMethod);
        defineIdentifiers(averageMethod, projection);
        calculateRequiredGrade(projection,averageMethod.trim());
        calculateFinalGrade(projection,averageMethod.trim());
    }

    @Transactional
    private void defineIdentifiers(String averageMethod, Projection projection) {
        String identifierRegex = "(?<!@)\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher matcher = pattern.matcher(averageMethod.replaceAll("\\s",""));
        while (matcher.find()){
            var assessment = new Assessment();
            Pattern p = Pattern.compile("(?<=\\[)(\\d+(([.,])?\\d+)?)");
            Matcher m = p.matcher(matcher.group());
            if(assessmentRepository.existsByProjectionAndIdentifier(projection,matcher.group().replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?","")))continue;
            if(m.find()) assessment.setMaxValue(Double.parseDouble(m.group().replaceAll(",",".")));
            assessment.setIdentifier(matcher.group().replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""));
            assessment.setProjection(projection);
            projection.addAssessment(assessmentRepository.save(assessment));

        }
    }

    public List<Assessment> listAssessment(Long userId, Long courseId, Long projectionId) {
        validateProjection(userId,courseId,projectionId);
        var projection = projectionRepository.findById(projectionId).orElseThrow();
        return assessmentRepository.findByProjection(projection);
    }

    @Transactional
    public Assessment insertGrade(Long userId, Long courseId, Long projectionId, Long id, DoubleRequestDTO gradeDto){
        validateProjection(userId,courseId,projectionId);
        var assessment = assessmentRepository.findByProjectionIdAndId(projectionId,id).orElseThrow(() -> new NotFoundArgumentException("Assessment id "+id+" not found for the Projection id "+projectionId));
        var course = courseRepository.findById(courseId).orElseThrow();
        var projection = projectionRepository.findById(projectionId).orElseThrow();

        if (gradeDto.value() <= assessment.getMaxValue())assessment.setGrade(gradeDto.value());
        else throw new IllegalArgumentException("It is not allowed to enter a grade higher than "+assessment.getMaxValue());
        assessment.setFixed(true);
        calculateFinalGrade(projectionRepository.findById(projectionId).orElseThrow(),course.getAverageMethod());
        calculateRequiredGrade(projection, course.getAverageMethod());
        return assessment;
    }

    @Transactional
    public void calculateFinalGrade(Projection projection, String averageMethod)  {

        ArrayList<String> polishNotation = convertToPolishNotation(averageMethod);
        Deque<Double> stackDouble = new ArrayDeque<>();
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < polishNotation.size(); i++) {
            String token = polishNotation.get(i);
            if(token.matches("(\\d+(([.,])?\\d+)?)"))stackDouble.push(Double.parseDouble(token.replaceAll(",",".")));
            else if(token.matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")){
                stackDouble.push(assessmentRepository.findByIndentifier(token.replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""),projection.getId()).getGrade());
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

                        if(maxValueInt > values.size()) throw new IllegalArgumentException("It is not possible to select more values than those provided");

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

    private ArrayList<String> convertToPolishNotation(String averageMethod) {

        ArrayList<String> methodTokens = compileRegex(averageMethod.trim());
        Deque<String> stack = new ArrayDeque<>();
        ArrayList<String> polishNotation = new ArrayList<>();
        for (int i = 0; i < methodTokens.size() ; i++) {
            String token = methodTokens.get(i);
            if(stack.isEmpty()&&token.matches("[*+/\\-]|@M(\\[\\d+]\\()?"))stack.push(token);// empty or token is a delimiter
            else{
                if (token.matches("[+\\-]")) {
                    while (!stack.isEmpty()) {
                        if (stack.peek().matches(";|\\(|@M(\\[\\d+]\\()?"))break;
                        polishNotation.add(stack.pop());
                    }
                    stack.push(token);
                }
                else if (token.matches("[*/]")) {
                    while (stack.peek().matches("[*/]"))
                        polishNotation.add(stack.pop());
                    stack.push(token);
                }
                else if (token.matches(";|\\(|@M(\\[\\d+]\\()?")) {
                    stack.push(token);
                }
                else if (token.matches("\\)")) {
                    while (!stack.peek().matches("\\(|@M(\\[\\d+]\\()?"))
                        polishNotation.add(stack.pop());
                    if(stack.peek().matches("\\("))stack.pop();
                    else if(stack.peek().matches("@M(\\[\\d+]\\()?"))polishNotation.add(stack.pop());
                }
                else {
                    polishNotation.add(token);
                }
            }
        }
        while (!stack.isEmpty()){
            if(stack.peek().matches("\\("))stack.pop();
            else polishNotation.add(stack.pop());
        }
        return polishNotation;
    }

    public void deleteAllAssessment(){
        assessmentRepository.deleteAll();
    }

    private ArrayList<String> compileRegex(String averageMethod) {
        //^(\d+(([.,])?\d+)?)(?=[\+\-\/\*])|(?<=[\(\+\-\*\/;])(\d+(([.,])?\d+)?)(?=[\)\/\*\+\-;])|(?<=[\+\-\*\/](\d+(([.,])?\d+)?)$|[\/\*\+\-\(\);]|(?<=[\/\*\+\-\)\(;])@M(\[\d+\]\()?|^@M(\[\d+\]\()?|(?<!@)\w*[A-Za-z]\w*(\[(\d+(([.,])?\d+)?)\])?
        String doubleRegex = "(\\d+(([.,])?\\d+)?)";
        String operatorsRegex = "\\+\\-\\*\\/";
        String featuresRegex = "@M";
        String coefficientsRegex = String.format("^%s(?=[%s])|(?<=[%s\\(;])%s(?=[%s\\);])|(?<=[%s])%s$",doubleRegex,operatorsRegex,operatorsRegex,doubleRegex,operatorsRegex,operatorsRegex,doubleRegex);
        String delimitersRegex = String.format("[%s\\(\\)\\;]|(?<=[%s\\)\\(;])%s(\\[\\d+\\]\\()?|^%s(\\[\\d+\\]\\()?",operatorsRegex,operatorsRegex,featuresRegex,featuresRegex);
        String identifierRegex = String.format("(?<!@)\\w*[A-Za-z]\\w*(\\[%s\\])?",doubleRegex);
        String regex = String.format("%s|%s|%s",coefficientsRegex,delimitersRegex,identifierRegex);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(averageMethod.replaceAll("\\s",""));

        ArrayList<String> methodTokens = new ArrayList<>();
        while (matcher.find()) {
            methodTokens.add(matcher.group());
        }
        if(averageMethod.replaceAll("\\s","").replaceAll(regex,"").isEmpty())return methodTokens;
        else {
            throw new IllegalArgumentException("Method for calculating averages not accepted, formula terms are invalid: "+averageMethod.replaceAll("\\s","").replaceAll(regex,"-"));
        }
    }

    @Transactional
    private void calculateRequiredGrade(Projection projection, String averageMethod){

        var polishNotation = convertToPolishNotation(averageMethod);
        double biggerMaxValue = assessmentRepository.getBiggerMaxValue(projection.getId());
        var course = projection.getCourse();
        double cutOffGrade = course.getCutOffGrade();
        double requiredGrade = 0;
        double result = 0;
        int i;
        for ( i = 0; i <= biggerMaxValue*100; i+=100) {
             requiredGrade = (double) i/100;
             result = simulate(requiredGrade, projection, polishNotation);
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
                result = simulate(requiredGrade, projection, polishNotation);
                if (result <= cutOffGrade) break;
            }
            if(result<cutOffGrade){
                for (int k = j+10; k <= j+10 ; k++) {
                    requiredGrade = (double) k/100;
                    result = simulate(requiredGrade,projection, polishNotation);
                    if(result>=cutOffGrade)break;
                }
            }
        }
        if(result<cutOffGrade){
            for (int j = 0; j < polishNotation.size(); j++) {
                if (polishNotation.get(j).matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")) {
                    var assessment = assessmentRepository.findByIndentifier(polishNotation.get(j).replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""), projection.getId());
                    if (!assessment.isFixed()) {
                        assessment.setRequiredGrade(assessment.getMaxValue());
                        assessmentRepository.save(assessment);
                    }
                    else assessment.setRequiredGrade(0.00);
                }
            }
            throw new FailedException("Affs! unfortunately it will not be possible to reach the cut-off-grade");
        }
        else {
            for (int j = 0; j < polishNotation.size(); j++) {
                if (polishNotation.get(j).matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")){
                    var assessment = assessmentRepository.findByIndentifier(polishNotation.get(j).replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""),projection.getId());
                    if (!assessment.isFixed()){
                        assessment.setRequiredGrade(Double.min(requiredGrade,assessment.getMaxValue()));
                        assessmentRepository.save(assessment);
                    }
                    else assessment.setRequiredGrade(0.00);
                }
            }
        }
    }

    private double simulate(double requiredGrade, Projection projection, ArrayList<String> polishNotation){

        Deque<Double> stackDouble = new ArrayDeque<>();
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < polishNotation.size(); i++) {
            String token = polishNotation.get(i);
            if(token.matches("(\\d+(([.,])?\\d+)?)"))stackDouble.push(Double.parseDouble(token.replaceAll(",",".")));
            else if(token.matches("\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?")){
                var assessment = assessmentRepository.findByIndentifier(token.replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?",""),projection.getId());
                if(assessment.isFixed())stackDouble.push(assessment.getGrade());
                else stackDouble.push(Double.min(requiredGrade,assessment.getMaxValue()));
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
                        case "/":
                            if(b==0)throw new IllegalArgumentException("Cannot divide by zero");
                            stackDouble.push(a/b);
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

                        if(maxValueInt > values.size()) throw new IllegalArgumentException("It is not possible to select more values than those provided");

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
        return stackDouble.getFirst();
    }

    private void validateProjection(Long userId, Long courseId, Long projectionId){
        if(!userRepository.existsById(userId))throw new NotFoundArgumentException("User id "+userId+" not found");
        var user = userRepository.findById(userId).orElseThrow();
        if(!courseRepository.existsByUserAndId(user,courseId)) throw new NotFoundArgumentException("Course id "+courseId+" not found for the user id "+userId);
        var course = courseRepository.findById(courseId).orElseThrow();
        if(!projectionRepository.existsByCourseAndId(course,projectionId))throw  new NotFoundArgumentException("Projection Id "+projectionId+" not found for the course id "+courseId);

    }
}
