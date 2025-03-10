package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateFinalGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ICalculateRequiredGrade;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IIdentifiersDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
class AssessmentServiceImplTest {



    @InjectMocks
    private AssessmentServiceImpl assessmentService;

    @Mock
    private ProjectionRepository projectionRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private IIdentifiersDefinition identifiersDefinition;
    @Mock
    private ICalculateFinalGrade calculateFinalGrade;
    @Mock
    private ICalculateRequiredGrade calculateRequiredGrade;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        var user = new Users();
        var course = new Course();
        course.setName("Test");
        course.setCutOffGrade(6);
        course.setUser(user);
        course.setAverageMethod("P1+(2*P2)");
        var projection = new Projection(course,"Test");
        course.setProjection(List.of(projection));
        projection.setFinalGrade(7.5);
        var assessment1 = new Assessment("P1",2.5,10,projection);
        var assessment2 = new Assessment("P2",2.5,10,projection);
        projection.setAssessment(List.of(assessment1,assessment2));


    }

    @Test
    @DisplayName("Should create assesment and calculate final grade and required grade")
    void createAssessmentSuccessfullyTest() {
        Long projectionId = 1L;
        var course = new Course();
        var projection = new Projection(course,"Test");
        var assessment1 = new Assessment("P1",2.5,10,projection);
        var assessment2 = new Assessment("P2",2.5,10,projection);
        projection.setAssessment(List.of(assessment1,assessment2));

        when(projectionRepository.existsById(projectionId)).thenReturn(true);
        when(projectionRepository.findById(projectionId)).thenReturn(Optional.of(projection));
        when(projectionRepository.save(Mockito.any(Projection.class))).thenReturn(projection);

        assessmentService.createAssessment(projectionId);

        verify(assessmentRepository).save(Mockito.any(Assessment.class));
        verify(projectionRepository).save(Mockito.any(Projection.class));
        verify(projectionRepository).findById(projectionId);

    }

    @Test
    void listAssessment() {
    }

    @Test
    void insertGrade() {
    }
}