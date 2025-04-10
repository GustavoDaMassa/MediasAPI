package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ISimulationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CalculateRequiredGradeImplTest {

    @Autowired
    @InjectMocks
    private CalculateRequiredGradeImpl calculateRequiredGrade;

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private IConvertToPolishNotation convertToPolishNotation;

    @Mock
    private ISimulationResult simulationResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("calculateRequiredGrade - Takes successive test to find the necessary grade with no one grade fixed")
    void calculateRequiredGradeSuccessfullyNoOneFixed() {
        var course = new Course(1L,null,"BD",null,"@M[2](p1;p2)",6);
        var assessment1 = new Assessment("p1",0,10,null);
        var assessment2 = new Assessment("p2",0,10,null);
        var projection = new Projection(1L,course, List.of(assessment1,assessment2),
                "Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        course.setProjection(List.of(projection));
        var polishNotation = new ArrayList<>(List.of("p1", "p2", ";", "@M[2]("));

        when(convertToPolishNotation.convertToPolishNotation(course.getAverageMethod())).thenReturn(polishNotation);
        when(assessmentRepository.getBiggerMaxValue(projection.getId())).thenReturn(10.0);
        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);
        when(simulationResult.simulate(eq(3.0),any(Projection.class),eq(polishNotation))).thenReturn(6.0);

        calculateRequiredGrade.calculateRequiredGrade(projection,course);

        assertEquals(3,assessment1.getRequiredGrade());
        assertEquals(3,assessment2.getRequiredGrade());
        verify(assessmentRepository,times(2)).save(any(Assessment.class));
    }

    @Test
    @DisplayName("calculateRequiredGrade - Takes successive test to find the necessary grade with one of the grades fixed")
    void calculateRequiredGradeSuccessfullyAnyFixed() {
        var course = new Course(1L,null,"BD",null,"p1+p2",6);
        var assessment1 = new Assessment("p1",2,10,null);
        assessment1.setFixed(true);
        var assessment2 = new Assessment("p2",0,10,null);
        var projection = new Projection(1L,course, List.of(assessment1,assessment2),
                "Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        course.setProjection(List.of(projection));
        var polishNotation = new ArrayList<>(List.of("p1", "p2", "+"));

        when(convertToPolishNotation.convertToPolishNotation(course.getAverageMethod())).thenReturn(polishNotation);
        when(assessmentRepository.getBiggerMaxValue(projection.getId())).thenReturn(10.0);
        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);
        when(simulationResult.simulate(eq(4.0),any(Projection.class),eq(polishNotation))).thenReturn(6.0);

        calculateRequiredGrade.calculateRequiredGrade(projection,course);

        assertEquals(0,assessment1.getRequiredGrade());
        assertEquals(4,assessment2.getRequiredGrade());
        verify(assessmentRepository,times(2)).save(any(Assessment.class));
    }
}