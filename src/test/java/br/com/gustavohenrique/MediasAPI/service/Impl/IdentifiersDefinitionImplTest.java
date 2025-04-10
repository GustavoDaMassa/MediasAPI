package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.AssessmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdentifiersDefinitionImplTest {

    @Autowired
    @InjectMocks
    private IdentifiersDefinitionImpl identifiersDefinition;

    @Mock
    private AssessmentRepository assessmentRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("defineIdentifiers - Create and save assessment for the average method identifiers")
    void defineIdentifiersSuccessfully() {

        var assessment1 = new Assessment("p1",5,10,null);
        var assessment2 = new Assessment("p2",7,10,null);
        var assessments = new ArrayList<>(List.of(assessment1, assessment2));
        var projection = new Projection(1L,null, assessments,"Projection test",0);
        assessment1.setProjection(projection);
        assessment2.setProjection(projection);
        var averageMethod = "(p1+p2)/2";

        when(assessmentRepository.existsByProjectionAndIdentifier(any(Projection.class),eq(assessment1.getIdentifier()))).thenReturn(false);
        when(assessmentRepository.existsByProjectionAndIdentifier(any(Projection.class),eq(assessment2.getIdentifier()))).thenReturn(false);
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment1);

        identifiersDefinition.defineIdentifiers(averageMethod,projection);

        assertEquals(assessment1,projection.getAssessment().get(0));
        assertEquals(assessment2,projection.getAssessment().get(1));
        verify(assessmentRepository,times(2)).save(any(Assessment.class));
    }

    @Test
    @DisplayName("defineIdentifiers - Create and save assessment when the identifier is repeated")
    void defineIdentifiersSuccessfullySameIdentifier() {

        var assessment1 = new Assessment("p1",5,10,null);
        var assessments = new ArrayList<>(List.of(assessment1));
        var projection = new Projection(1L,null, assessments,"Projection test",0);
        assessment1.setProjection(projection);
        var averageMethod = "(p1+p1)/2";

        when(assessmentRepository.existsByProjectionAndIdentifier(any(Projection.class),eq(assessment1.getIdentifier()))).thenReturn(true);
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment1);

        identifiersDefinition.defineIdentifiers(averageMethod,projection);

        verify(assessmentRepository,never()).save(any(Assessment.class));
    }

    @Test
    @DisplayName("defineIdentifiers - Isn't created with no identifiers")
    void defineIdentifiersSuccessfullyNoIdentifiers() {

        var projection = new Projection();
        var averageMethod = "(2+2)/2";

        identifiersDefinition.defineIdentifiers(averageMethod,projection);

        verify(assessmentRepository,never()).save(any(Assessment.class));
        verify(assessmentRepository,never()).existsByProjectionAndIdentifier(any(Projection.class),anyString());
    }
}