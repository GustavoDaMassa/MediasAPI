package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
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
import static org.mockito.Mockito.*;

class SimulationImplTest {

    @Autowired
    @InjectMocks
    private SimulationImpl simulation;

    @Mock
    private AssessmentRepository assessmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("simulate - return the result of simulation with a grade test")
    void simulateSuccessfully() {
        var assessment1 = new Assessment("p1",0,10,null);
        var assessment2 = new Assessment("p2",0,10,null);
        var projection = new Projection(1L,null, List.of(assessment1,assessment2),"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "p2", ";", "@M[2]("));

        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);

        var response = simulation.simulate(2.3,projection,polishNotation);

        assertEquals(4.6,response);
        verify(assessmentRepository,times(2)).findByIndentifier(anyString(),anyLong());
    }

    @Test
    @DisplayName("simulate - return an exception when the has no identifiers")
    void simulateNoIdentifiers() {
        var assessment1 = new Assessment("p1",0,10,null);
        var projection = new Projection(1L,null, List.of(assessment1),"Projection test",0);
        assessment1.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "+", "+"));

        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);

        assertThrows(NoSuchElementException.class, ()-> simulation.simulate(2.3,projection,polishNotation));

        verify(assessmentRepository,times(1)).findByIndentifier(anyString(),anyLong());
    }

    @Test
    @DisplayName("simulate - return an exception when divide by zero")
    void simulateDivideByZero() {
        var assessment1 = new Assessment("p1",1,10,null);
        var assessment2 = new Assessment("p2",0,10,null);
        var projection = new Projection(1L,null, List.of(assessment1,assessment2),"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "p2", "/"));

        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);

        assertThrows(IllegalArgumentException.class, ()-> simulation.simulate(0,projection,polishNotation));

        verify(assessmentRepository,times(2)).findByIndentifier(anyString(),anyLong());
    }

    @Test
    @DisplayName("simulate - Should return an exception when the selection of grade is more than those provided")
    void simulateMorethanThoseProvided() {
        var assessment1 = new Assessment("p1",0,10,null);
        var assessment2 = new Assessment("p2",0,10,null);
        var projection = new Projection(1L,null, List.of(assessment1,assessment2),"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "p2", ";", "@M[5]("));

        when(assessmentRepository.findByIndentifier(assessment1.getIdentifier(),projection.getId())).thenReturn(assessment1);
        when(assessmentRepository.findByIndentifier(assessment2.getIdentifier(),projection.getId())).thenReturn(assessment2);

        assertThrows(IllegalArgumentException.class, ()-> simulation.simulate(0,projection,polishNotation));

        verify(assessmentRepository,times(2)).findByIndentifier(anyString(),anyLong());
    }
}