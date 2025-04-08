package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.IConvertToPolishNotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalculateFinalGradeImplTest {

    @Autowired
    @InjectMocks
    private CalculateFinalGradeImpl calculateFinalGrade;

    @Mock
    private ProjectionRepository projectionRepository;

    @Mock
    private  AssessmentRepository assessmentRepository;

    @Mock
    private  IConvertToPolishNotation convertToPolishNotation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("calculateResult - must do the calculation correctly")
    void calculateResultSuccessfully() {
        var assessment1 = new Assessment("p1",5,10,null);
        var assessment2 = new Assessment("p2",7,10,null);
        var projection = new Projection(1L,null, List.of(assessment1,assessment2),"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var averageMethod = "@M[2](p1;p2)";
        var polishNotation = new ArrayList<>(List.of("p1", "p2", ";", "@M[2]("));

        when(convertToPolishNotation.convertToPolishNotation(averageMethod)).thenReturn(polishNotation);
        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);
        when(projectionRepository.save(any(Projection.class))).thenReturn(projection);

        calculateFinalGrade.calculateResult(projection,averageMethod);

        assertEquals(12,projection.getFinalGrade());
        verify(projectionRepository).save(projection);
    }

    @Test
    @DisplayName("calculateResult - Should return an exception when the selection of grade is more than those provided")
    void calculateResultFailureMorethoseProvided() {
        var assessment1 = new Assessment("p1",5,10,null);
        var assessment2 = new Assessment("p2",7,10,null);
        var projection = new Projection(1L,null, List.of(assessment1,assessment2),"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var averageMethod = "@M[5](p1;p2)";
        var polishNotation = new ArrayList<>(List.of("p1", "p2", ";", "@M[5]("));

        when(convertToPolishNotation.convertToPolishNotation(averageMethod)).thenReturn(polishNotation);
        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);

        assertThrows(IllegalArgumentException.class, () ->calculateFinalGrade.calculateResult(projection,averageMethod));

        verify(projectionRepository,never()).save(projection);
    }

    @Test
    @DisplayName("calculateResult - Should return an exception when an operator is entered as a identifiers")
    void calculateResultFailureOperatorAsIdentifier() {
        var assessment1 = new Assessment("p1",5,10,null);
        var assessment2 = new Assessment("p2",7,10,null);
        var projection = new Projection(1L,null, List.of(assessment1,assessment2),"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var averageMethod = "@M[2](p1;+)";
        var polishNotation = new ArrayList<>(List.of("p1", "+", ";", "@M[2]("));

        when(convertToPolishNotation.convertToPolishNotation(averageMethod)).thenReturn(polishNotation);
        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);

        assertThrows(NoSuchElementException.class, () ->calculateFinalGrade.calculateResult(projection,averageMethod));

        verify(projectionRepository,never()).save(projection);
    }
}