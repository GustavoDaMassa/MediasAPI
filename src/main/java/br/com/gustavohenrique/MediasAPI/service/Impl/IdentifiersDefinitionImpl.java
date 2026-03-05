package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IIdentifiersDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class IdentifiersDefinitionImpl implements IIdentifiersDefinition {

    private final AssessmentRepository assessmentRepository;
    @Autowired
    public IdentifiersDefinitionImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional
    public void defineIdentifiers(String averageMethod, Projection projection) {
        String identifierRegex = "(?<!@)\\w*[A-Za-z]\\w*(\\[(\\d+(([.,])?\\d+)?)])?";
        Pattern pattern = Pattern.compile(identifierRegex);
        Matcher matcher = pattern.matcher(averageMethod.replaceAll("\\s",""));
        while (matcher.find()){
            String identifier = matcher.group().replaceAll("(\\[(\\d+(([.,])?\\d+)?)])?", "");
            if (assessmentRepository.existsByProjectionAndIdentifier(projection, identifier)) continue;
            Pattern p = Pattern.compile("(?<=\\[)(\\d+(([.,])?\\d+)?)");
            Matcher m = p.matcher(matcher.group());
            double maxValue = m.find() ? Double.parseDouble(m.group().replaceAll(",", ".")) : 10.0;
            var assessment = new Assessment(identifier, maxValue, projection);
            projection.addAssessment(assessmentRepository.save(assessment));
        }
    }
}
