package br.com.gustavohenrique.MediasAPI.config;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

    @PostConstruct
    public void UpDB(){
        Users users1 = new Users(null,"Gustavo","gustavo.pereira@discente.ufg.br","aula321");
        Users users2 = new Users(null,"Henrique","gustavohenrique3gb@gmail.com.br","aula321");
        userRepository.saveAll(List.of(users1, users2));

        Course course1 = new Course(null, users1.getId(), "Banco de Dados","0.6*Prova+0.4*Seminário",6.00);
        Course course2 = new Course(null, users1.getId(), "Sistemas Gerenciadores de Banco de Dados","@M[3](P1;P2;P3;P4)/3",6.00);
        courseRepository.saveAll(List.of(course1,course2));

        Projection projection1 = new Projection(course1.getId(), course1.getName());
        Projection projection2 = new Projection(course2.getId(), course2.getName());
        projectionRepository.saveAll(List.of(projection1,projection2));

        Assessment assessment1 = new Assessment("Prova",0.00,10.0,projection1);
        Assessment assessment2 = new Assessment("Seminário",0.00,10.0,projection1);
        Assessment assessmentsgbd1 = new Assessment("P1",0.00,10.00,projection2);
        Assessment assessmentsgbd2 = new Assessment("P2",0.00,10.00,projection2);
        Assessment assessmentsgbd3 = new Assessment("P3",0.00,10.00,projection2);
        Assessment assessmentsgbd4 = new Assessment("P4",0.00,10.00,projection2);
        assessmentRepository.saveAll(List.of(assessment1,assessment2,assessmentsgbd1,assessmentsgbd2,assessmentsgbd3,assessmentsgbd4));

        projection1.setAssessment(List.of(assessment1,assessment2));
        projection2.setAssessment(List.of(assessmentsgbd1,assessmentsgbd2,assessmentsgbd3,assessmentsgbd4));
        projectionRepository.saveAll(List.of(projection1,projection2));
    }
}
