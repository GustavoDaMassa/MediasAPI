package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AssessmentServiceImplTest {

    @Autowired
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
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(projectionRepository.existsById(anyLong())).thenReturn(true);
    }

    @Test
    @DisplayName("createAssessment - Should call others method to create an assessment successfully")
    void createAssessmentSuccessfully() {
        var projection = new Projection(2L,new Course(1L,null,"BD",null,
                "P1+P@",6),null,"Projection default",10);

        doNothing().when(identifiersDefinition)
                .defineIdentifiers(Mockito.eq(projection.getCourse().getAverageMethod()),Mockito.any(Projection.class));
        doNothing().when(calculateFinalGrade)
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        doNothing().when(calculateRequiredGrade)
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

        assessmentService.createAssessment(projection);

        verify(identifiersDefinition)
                .defineIdentifiers(Mockito.eq(projection.getCourse().getAverageMethod()),Mockito.any(Projection.class));
        verify(calculateFinalGrade)
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        verify(calculateRequiredGrade)
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));
    }

    @Test
    @DisplayName("listAssessment - Return a list of assessment successfully")
    void listAssessmentSuccessfully() {
        var assessment1 = new Assessment("P1",0,10,null);
        var assessment2 = new Assessment("P@",0,10,null);
        var projection = new Projection(1L,null,
                List.of(assessment1,assessment2),"Projection test",9.8);

        when(projectionRepository.findById(projection.getId())).thenReturn(Optional.of(projection));
        when(assessmentRepository.findByProjection(any(Projection.class))).thenReturn(projection.getAssessment());

        var response = assessmentService.listAssessment(projection.getId());

        AssertAssessment(assessment1, response.get(0));
        AssertAssessment(assessment2, response.get(1));
        verify(projectionRepository).findById(projection.getId());
        verify(assessmentRepository).findByProjection(any(Projection.class));

    }

    @Test
    @DisplayName("listAssessment - Return an exception when projection not exist")
    void listAssessmentProjectionNotFound() {
        var projection = new Projection(1L,null,null,"Projection test",9.8);

        when(projectionRepository.findById(projection.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,()-> assessmentService.listAssessment(projection.getId()));

        verify(projectionRepository).findById(projection.getId());
        verify(assessmentRepository,never()).findByProjection(any(Projection.class));

    }

    @Test
    @DisplayName("insertGrade - Should return the assesment with the grade inserted successfully")
    void insertGradeSuccessfully() {
        var gradeDto = new DoubleRequestDTO(8);
        var assessment = new Assessment("P1",0,10,null);
        var projection = new Projection(1L,null,List.of(assessment),"projection test",0);
        assessment.setProjection(projection);
        var course = new Course(1L,null,"BD",List.of(projection),"P1+P@",6);
        projection.setCourse(course);

        when(projectionRepository.findById(projection.getId())).thenReturn(Optional.of(projection));
        when(assessmentRepository.findByProjectionIdAndId(projection.getId(), assessment.getId()))
                .thenReturn(Optional.of(assessment));
        doNothing().when(calculateFinalGrade)
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        doNothing().when(calculateRequiredGrade)
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

        var response = assessmentService.insertGrade(projection.getId(),assessment.getId(),gradeDto);

        AssertAssessment(assessment,response);
        verify(projectionRepository).findById(projection.getId());
        verify(assessmentRepository).findByProjectionIdAndId(projection.getId(),assessment.getId());
        verify(calculateFinalGrade)
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        verify(calculateRequiredGrade)
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

    }

    @Test
    @DisplayName("insertGrade - Should return exception when projection not exist")
    void insertGradeProjectionNotFound() {
        var gradeDto = new DoubleRequestDTO(8);
        var assessment = new Assessment("P1",0,10,null);
        var projection = new Projection(1L,null,List.of(assessment),"projection test",0);
        assessment.setProjection(projection);
        var course = new Course(1L,null,"BD",List.of(projection),"P1+P@",6);
        projection.setCourse(course);

        when(projectionRepository.findById(projection.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,
                ()-> assessmentService.insertGrade(projection.getId(),assessment.getId(),gradeDto));


        verify(projectionRepository).findById(projection.getId());
        verify(assessmentRepository,never()).findByProjectionIdAndId(projection.getId(),assessment.getId());
        verify(calculateFinalGrade,never())
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        verify(calculateRequiredGrade,never())
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

    }

    @Test
    @DisplayName("insertGrade - Should return exception when assessment not exist")
    void insertGradeAssessmentNotFound() {
        var gradeDto = new DoubleRequestDTO(8);
        var assessment = new Assessment("P1",0,10,null);
        var projection = new Projection(1L,null,List.of(assessment),"projection test",0);
        assessment.setProjection(projection);
        var course = new Course(1L,null,"BD",List.of(projection),"P1+P@",6);
        projection.setCourse(course);

        when(projectionRepository.findById(projection.getId())).thenReturn(Optional.of(projection));
        when(assessmentRepository.findByProjectionIdAndId(projection.getId(), assessment.getId()))
                .thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,
                ()-> assessmentService.insertGrade(projection.getId(),assessment.getId(),gradeDto));


        verify(projectionRepository).findById(projection.getId());
        verify(assessmentRepository).findByProjectionIdAndId(projection.getId(),assessment.getId());
        verify(calculateFinalGrade,never())
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        verify(calculateRequiredGrade,never())
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

    }

    @Test
    @DisplayName("insertGrade - Should return exception when de new value is bigger than the max value allowed")
    void insertGradeFailureGradeBiggerthanMaxValue() {
        var gradeDto = new DoubleRequestDTO(12);
        var assessment = new Assessment("P1",0,10,null);
        var projection = new Projection(1L,null,List.of(assessment),"projection test",0);
        assessment.setProjection(projection);
        var course = new Course(1L,null,"BD",List.of(projection),"P1+P@",6);
        projection.setCourse(course);

        when(projectionRepository.findById(projection.getId())).thenReturn(Optional.of(projection));
        when(assessmentRepository.findByProjectionIdAndId(projection.getId(), assessment.getId()))
                .thenReturn(Optional.of(assessment));
        doNothing().when(calculateFinalGrade)
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        doNothing().when(calculateRequiredGrade)
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

        assertThrows(IllegalArgumentException.class,()-> assessmentService
                .insertGrade(projection.getId(),assessment.getId(),gradeDto));

        verify(projectionRepository).findById(projection.getId());
        verify(assessmentRepository).findByProjectionIdAndId(projection.getId(),assessment.getId());
        verify(calculateFinalGrade,never())
                .calculateResult(any(Projection.class),eq(projection.getCourse().getAverageMethod()));
        verify(calculateRequiredGrade,never())
                .calculateRequiredGrade(any(Projection.class),eq(projection.getCourse()));

    }

    private static void AssertAssessment(Assessment projection, Assessment response) {
        assertEquals(projection.getId(), response.getId());
        assertEquals(projection.getProjection(), response.getProjection());
        assertEquals(projection.getMaxValue(), response.getMaxValue());
        assertEquals(projection.getGrade(), response.getGrade());
        assertEquals(projection.getIdentifier(), response.getIdentifier());
        assertEquals(projection.getRequiredGrade(), response.getRequiredGrade());
        assertEquals(projection.getClass(), response.getClass());
    }
}