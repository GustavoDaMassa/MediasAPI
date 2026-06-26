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
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CalculateRequiredGradeMaxNearImplTest {

    @InjectMocks
    private CalculateRequiredGradeMaxNearImpl calculateRequiredGradeMaxNear;

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
    @DisplayName("Unreachable cutoff - all unfixed get -1, fixed get 0")
    void unreachableCutoff() {
        var course = new Course(1L, null, "BD", null, "p1+p2", 6);
        var p1 = new Assessment("p1", 0, 10, null);
        var p2 = new Assessment("p2", 0, 10, null);
        var projection = new Projection(1L, course, List.of(p1, p2), "Proj", 0);
        p1.setProjection(projection);
        p2.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "p2", "+"));

        when(convertToPolishNotation.convertToPolishNotation(course.getAverageMethod())).thenReturn(polishNotation);
        when(assessmentRepository.findByIndentifier("p1", projection.getId())).thenReturn(p1);
        when(assessmentRepository.findByIndentifier("p2", projection.getId())).thenReturn(p2);
        // simulate(MAX_VALUE, ...) not mocked → returns 0 < cutOff → unreachable

        calculateRequiredGradeMaxNear.calculateRequiredGradeMaxNear(projection, course);

        assertEquals(-1.0, p1.getRequiredGradeMaxNear());
        assertEquals(-1.0, p2.getRequiredGradeMaxNear());
        verify(assessmentRepository, times(2)).save(any(Assessment.class));
    }

    @Test
    @DisplayName("Single unfixed assessment - requiredGradeMaxNear equals the uniform required grade")
    void singleUnfixedAssessment() {
        var course = new Course(1L, null, "BD", null, "p1", 6);
        var p1 = new Assessment("p1", 0, 10, null);
        var projection = new Projection(1L, course, List.of(p1), "Proj", 0);
        p1.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1"));

        when(convertToPolishNotation.convertToPolishNotation(course.getAverageMethod())).thenReturn(polishNotation);
        when(assessmentRepository.getBiggerMaxValue(projection.getId())).thenReturn(10.0);
        when(assessmentRepository.findByIndentifier("p1", projection.getId())).thenReturn(p1);
        when(simulationResult.simulate(Double.MAX_VALUE, projection, polishNotation)).thenReturn(10.0);
        when(simulationResult.simulateWithMaxed(anyDouble(), any(), any(), any())).thenAnswer(inv -> {
            double grade = inv.getArgument(0);
            return grade >= 6.0 ? 6.0 : 0.0;
        });

        calculateRequiredGradeMaxNear.calculateRequiredGradeMaxNear(projection, course);

        assertEquals(6.0, p1.getRequiredGradeMaxNear());
        verify(assessmentRepository).save(p1);
    }

    @Test
    @DisplayName("Two unfixed assessments - second requires less when first is maxed")
    void twoUnfixedDecreasingSequence() {
        var course = new Course(1L, null, "BD", null, "p1+p2", 12);
        var p1 = new Assessment("p1", 0, 10, null);
        var p2 = new Assessment("p2", 0, 10, null);
        var projection = new Projection(1L, course, List.of(p1, p2), "Proj", 0);
        p1.setProjection(projection);
        p2.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "p2", "+"));

        when(convertToPolishNotation.convertToPolishNotation(course.getAverageMethod())).thenReturn(polishNotation);
        when(assessmentRepository.getBiggerMaxValue(projection.getId())).thenReturn(10.0);
        when(assessmentRepository.findByIndentifier("p1", projection.getId())).thenReturn(p1);
        when(assessmentRepository.findByIndentifier("p2", projection.getId())).thenReturn(p2);
        when(simulationResult.simulate(Double.MAX_VALUE, projection, polishNotation)).thenReturn(20.0);
        when(simulationResult.simulateWithMaxed(anyDouble(), any(), any(), any())).thenAnswer(inv -> {
            double grade = inv.getArgument(0);
            Set<String> maxed = inv.getArgument(3);
            // p1 not maxed: need 6.0 on both → sum >= 12 when grade >= 6
            // p1 maxed (contributes 10): need 2.0 on p2 → sum >= 12 when grade >= 2
            return maxed.contains("p1") ? (grade >= 2.0 ? 12.0 : 0.0)
                                        : (grade >= 6.0 ? 12.0 : 0.0);
        });

        calculateRequiredGradeMaxNear.calculateRequiredGradeMaxNear(projection, course);

        assertEquals(6.0, p1.getRequiredGradeMaxNear());
        assertEquals(2.0, p2.getRequiredGradeMaxNear());
        assertTrue(p2.getRequiredGradeMaxNear() <= p1.getRequiredGradeMaxNear());
        verify(assessmentRepository, times(2)).save(any(Assessment.class));
    }

    @Test
    @DisplayName("Fixed assessment gets 0.0, its max grade helps the following unfixed")
    void fixedAssessmentSkippedAndHelpsNext() {
        var course = new Course(1L, null, "BD", null, "p1+p2", 12);
        var p1 = new Assessment("p1", 0, 10, null);
        p1.applyGrade(8.0);
        var p2 = new Assessment("p2", 0, 10, null);
        var projection = new Projection(1L, course, List.of(p1, p2), "Proj", 0);
        p1.setProjection(projection);
        p2.setProjection(projection);
        var polishNotation = new ArrayList<>(List.of("p1", "p2", "+"));

        when(convertToPolishNotation.convertToPolishNotation(course.getAverageMethod())).thenReturn(polishNotation);
        when(assessmentRepository.getBiggerMaxValue(projection.getId())).thenReturn(10.0);
        when(assessmentRepository.findByIndentifier("p1", projection.getId())).thenReturn(p1);
        when(assessmentRepository.findByIndentifier("p2", projection.getId())).thenReturn(p2);
        when(simulationResult.simulate(Double.MAX_VALUE, projection, polishNotation)).thenReturn(18.0);
        when(simulationResult.simulateWithMaxed(anyDouble(), any(), any(), any())).thenAnswer(inv -> {
            double grade = inv.getArgument(0);
            // p1 is fixed at 8; p2 needs 4 to reach 12
            return grade >= 4.0 ? 12.0 : 0.0;
        });

        calculateRequiredGradeMaxNear.calculateRequiredGradeMaxNear(projection, course);

        assertEquals(0.0, p1.getRequiredGradeMaxNear());
        assertEquals(4.0, p2.getRequiredGradeMaxNear());
        verify(assessmentRepository, times(2)).save(any(Assessment.class));
    }
}
