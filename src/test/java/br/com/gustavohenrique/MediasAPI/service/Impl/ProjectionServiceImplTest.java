package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
import org.h2.command.dml.MergeUsing;
import org.hibernate.validator.constraints.Mod10Check;
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
import static org.mockito.Mockito.*;

class ProjectionServiceImplTest {

    @Autowired
    @InjectMocks
    private ProjectionServiceImpl projectionService;

    @Mock
    private AssessmentService assessmentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProjectionRepository projectionRepository;

    private final Long courseId = 1L;
    private final Course course = new Course(courseId,null,"BD",null,
            "P1+P2",6);
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    }

    @Test
    @DisplayName("createProjection - Should return the projection created successfully")
    void createProjectionSuccessfully() {
        var projectionName = new StringRequestDTO("Projection default");
        var projection = new Projection(18L,null,null,projectionName.string(),10);

        when(projectionRepository.existsByCourseAndName(course,projectionName.string())).thenReturn(false);
        when(projectionRepository.save(any(Projection.class))).thenReturn(projection);
        doNothing().when(assessmentService).createAssessment(any(Projection.class));

        var response = projectionService.createProjection(courseId,projectionName);

        AssertProjection(projection, response);
        verify(courseRepository).findById(courseId);
        verify(projectionRepository).existsByCourseAndName(course,projectionName.string());
        verify(projectionRepository).save(any(Projection.class));
        verify(assessmentService).createAssessment(any(Projection.class));
    }

    @Test
    @DisplayName("createProjection - Should return exception when the name chosen already exist")
    void createProjectionDataIntegrity() {
        var projectionName = new StringRequestDTO("Projeçtion default");
        var projection = new Projection(null,null,null,projectionName.string(),10);

        when(projectionRepository.existsByCourseAndName(course,projectionName.string()))
                .thenThrow(DataIntegrityException.class);

        assertThrows(DataIntegrityException.class, () -> projectionService.createProjection(courseId,projectionName));

        verify(courseRepository).findById(courseId);
        verify(projectionRepository).existsByCourseAndName(course,projectionName.string());
        verify(projectionRepository,never()).save(any(Projection.class));
        verify(assessmentService,never()).createAssessment(any(Projection.class));
    }

    @Test
    @DisplayName("listProjecton - Should return a list of courses successfully")
    void listProjectionSuccessfully() {
        var projection1 = new Projection(1L,null,null,"Projection1",0);
        var projection2 = new Projection(2L,null,null,"Projection2",10);

        when(projectionRepository.findByCourse(course)).thenReturn(List.of(projection1,projection2));

        var response = projectionService.listProjection(courseId);

        AssertProjection(projection1,response.get(0));
        AssertProjection(projection2,response.get(1));
        verify(courseRepository).findById(courseId);
        verify(projectionRepository).findByCourse(course);
    }

    @Test
    @DisplayName("updateProjectionName - Should return the proojection with the name updated")
    void updateProjectionNameSuccessfully() {
        var newProjectionName = new StringRequestDTO("new Projection");
        var projection = new Projection(1L,null,null, newProjectionName.string(),10);

        when(projectionRepository.existsByCourseAndName(course, newProjectionName.string())).thenReturn(false);
        when(projectionRepository.findByCourseAndId(course,projection.getId())).thenReturn(Optional.of(projection));

        var response = projectionService.updateProjectionName(courseId, projection.getId(), newProjectionName);

        AssertProjection(projection,response);
        verify(courseRepository).findById(courseId);
        verify(projectionRepository).existsByCourseAndName(course, newProjectionName.string());
        verify(projectionRepository).findByCourseAndId(course, projection.getId());
    }

    @Test
    @DisplayName("updateProjectionName - Should return an exception when the name chosen already exist")
    void updateProjectionNameDataIntegrity() {
        var newProjectionName = new StringRequestDTO("new Projection");
        var projection = new Projection(1L,null,null, newProjectionName.string(),10);

        when(projectionRepository.existsByCourseAndName(course, newProjectionName.string()))
                .thenThrow(DataIntegrityException.class);

        assertThrows(DataIntegrityException.class,
                ()-> projectionService.updateProjectionName(courseId,projection.getId(),newProjectionName));

        verify(courseRepository).findById(courseId);
        verify(projectionRepository).existsByCourseAndName(course, newProjectionName.string());
        verify(projectionRepository,never()).findByCourseAndId(course, projection.getId());
    }

    @Test
    @DisplayName("updateProjectionName - Should return an exception when the projection not exist")
    void updateProjectionNameProjectionNotFound() {
        var newProjectionName = new StringRequestDTO("new Projection");
        var projection = new Projection(1L,null,null, newProjectionName.string(),10);

        when(projectionRepository.findByCourseAndId(course, projection.getId()))
                .thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,
                ()-> projectionService.updateProjectionName(courseId,projection.getId(),newProjectionName));

        verify(courseRepository).findById(courseId);
        verify(projectionRepository).existsByCourseAndName(course, newProjectionName.string());
        verify(projectionRepository).findByCourseAndId(course, projection.getId());
    }

    @Test
    @DisplayName("deleteProjection - Should return the projection deleted")
    void deleteProjectionSuccessfully() {
        var projection = new Projection(1L,null,null,"BD",3);

        when(projectionRepository.findByCourseAndId(course,projection.getId())).thenReturn(Optional.of(projection));
        doNothing().when(projectionRepository).deleteProjection(projection.getId());

        var response = projectionService.deleteProjection(courseId, projection.getId());

        AssertProjection(projection,response);
        verify(courseRepository).findById(courseId);
        verify(projectionRepository).findByCourseAndId(course,projection.getId());
        verify(projectionRepository).deleteProjection(projection.getId());
    }

    @Test
    @DisplayName("deleteProjection - Should return an exception when projection not exist")
    void deleteProjectionNotFound() {
        var projection = new Projection(1L,null,null,"BD",3);

        when(projectionRepository.findByCourseAndId(course,projection.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,()-> projectionService.deleteProjection(courseId,projection.getId()));

        verify(courseRepository).findById(courseId);
        verify(projectionRepository).findByCourseAndId(course,projection.getId());
        verify(projectionRepository,never()).deleteProjection(projection.getId());
    }

    @Test
    @DisplayName("deleteAllProjection - Should delete all projection successfully")
    void deleteAllProjectionsSuccessfully() {

        doNothing().when(projectionRepository).deleteAllByCourse(courseId);

        projectionService.deleteAllProjections(courseId);

        verify(courseRepository).existsById(courseId);
        verify(projectionRepository).deleteAllByCourse(courseId);
    }

    @Test
    @DisplayName("deleteAllProjection - Should return exception when the user not exist")
    void deleteAllProjectionUserNotFound(){

        when(courseRepository.existsById(courseId)).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,()-> projectionService.deleteAllProjections(courseId));

        verify(courseRepository).existsById(courseId);
        verify(projectionRepository,never()).deleteAllByCourse(courseId);
    }

    @Test
    @DisplayName("listAllProjection - Should return a list of projection by the user id")
    void listAllProjectionSuccessfully() {
        var userId = 1L;
        var projection1 = new Projection(1L,null,null,"Projection1",0);
        var projection2 = new Projection(2L,null,null,"Projection2",10);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(projectionRepository.findAllByUserId(userId)).thenReturn(List.of(projection1,projection2));

        var response = projectionService.listAllProjection(userId);

        AssertProjection(projection1,response.get(0));
        AssertProjection(projection2,response.get(1));
        verify(userRepository).existsById(userId);
        verify(projectionRepository).findAllByUserId(userId);

    }

    @Test
    @DisplayName("listAllProjection - Should return exception when the user not exist")
    void listAllProjectionUserNotFound(){

        when(userRepository.existsById(anyLong())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,()-> projectionService.listAllProjection(anyLong()));

        verify(userRepository).existsById(anyLong());
        verify(projectionRepository,never()).findAllByUserId(anyLong());
    }

    @Test
    @DisplayName("All methods - Return exception when course id not found in the all methods")
    void courseIdNotFound(){
        var projectionName = new StringRequestDTO("Projection default");
        var projection = new Projection(null,null,null,projectionName.string(),10);

        when(courseRepository.findById(courseId)).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> projectionService.createProjection(courseId,projectionName));
        assertThrows(NotFoundArgumentException.class, () -> projectionService.listProjection(courseId));
        assertThrows(NotFoundArgumentException.class,
                () -> projectionService.updateProjectionName(courseId,projection.getId(),projectionName));
        assertThrows(NotFoundArgumentException.class,
                () -> projectionService.deleteProjection(courseId,projection.getId()));

        verify(courseRepository,times(4)).findById(courseId);
        verify(projectionRepository,never()).existsByCourseAndName(course,projectionName.string());
        verify(projectionRepository,never()).save(any(Projection.class));
        verify(assessmentService,never()).createAssessment(any(Projection.class));
        verify(projectionRepository,never()).findByCourse(course);
        verify(projectionRepository,never()).findByCourseAndId(course,projection.getId());
    }

    private static void AssertProjection(Projection projection, Projection response) {
        assertEquals(projection.getId(), response.getId());
        assertEquals(projection.getName(), response.getName());
        assertEquals(projection.getCourse(), response.getCourse());
        assertEquals(projection.getAssessment(), response.getAssessment());
        assertEquals(projection.getFinalGrade(), response.getFinalGrade());
        assertEquals(projection.getClass(), response.getClass());
    }
}