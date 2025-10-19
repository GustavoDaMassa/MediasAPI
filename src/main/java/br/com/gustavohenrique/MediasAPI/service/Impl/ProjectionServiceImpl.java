package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import jakarta.validation.Valid;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectionServiceImpl implements ProjectionService {

    private final AssessmentService assessmentService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ProjectionRepository projectionRepository;
    private final UserService userService;
    @Autowired
    public ProjectionServiceImpl(AssessmentService assessmentService, UserRepository userRepository,
                                 CourseRepository courseRepository, ProjectionRepository projectionRepository, UserService userService) {
        this.assessmentService = assessmentService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.projectionRepository = projectionRepository;
        this.userService = userService;
    }

    @Transactional
    public Projection createProjection(Long courseId, @Valid StringRequestDTO projectionName){
        validateCourse(courseId);
        var course = courseRepository.findById(courseId).orElseThrow();
        if (projectionRepository.existsByCourseAndName(course,projectionName.string())) {
            throw new DataIntegrityException(projectionName.string());
        }
        var projection = new Projection(course,projectionName.string());
        projectionRepository.save(projection);
        assessmentService.createAssessment(projection);
        return projection;
    }

    public List<Projection> listProjection(Long courseId) {
        validateCourse(courseId);
        Course course = courseRepository.findById(courseId).orElseThrow();
        return projectionRepository.findByCourse(course);
    }

    @Transactional
    public Projection updateProjectionName(Long courseId, Long id, StringRequestDTO newProjectionName) {
        validateCourse(courseId);
        var course = courseRepository.findById(courseId).orElseThrow();
        if (projectionRepository.existsByCourseAndName(course,newProjectionName.string()))
            throw new DataIntegrityException(newProjectionName.string());
        var projection = projectionRepository.findByCourseAndId(course,id).orElseThrow
                (()-> new NotFoundArgumentException("Projection id "+id+" not found for the course id "+courseId));
        projection.setName(newProjectionName.string());
        return projection;
    }

    @Transactional
    public Projection deleteProjection(Long courseId, Long id) {
        validateCourse(courseId);
        var course = courseRepository.findById(courseId).orElseThrow();
        var projection = projectionRepository.findByCourseAndId(course,id).orElseThrow
                (()-> new NotFoundArgumentException("Projection id "+id+" not found for the course id "+courseId));
        projectionRepository.deleteProjection(id, courseId);
        return projection;
    }

    @Transactional
    public void deleteAllProjections(Long courseId, Long userId) {
        validateCourse(courseId);
        projectionRepository.deleteAllByCourse(courseId, userId);
    }

    public List<Projection> listAllProjection(Long userId) {
        if(!userRepository.existsById(userId))throw new NotFoundArgumentException("User id "+ userId +" not found");
        return projectionRepository.findAllByUserId(userId);
    }

    private void validateCourse(Long courseId){
        if(!courseRepository.existsById(courseId))
            throw new NotFoundArgumentException("Course id "+courseId+" not found");
    }

    @Override
    public void getAuthenticatedUserByCourseId(Long courseId) {
        var user = userService.getAuthenticatedUser();
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundArgumentException("Course id " + courseId + " not found"));
        if (!course.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this resource.");
        }
    }
}

