package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectionService {

    @Autowired
    private AssessmentService assessmentService;

    private final UserRepository userRepository;
    private  final CourseRepository courseRepository;
    private  final ProjectionRepository projectionRepository;

    public ProjectionService(UserRepository userRepository, CourseRepository courseRepository,
                             ProjectionRepository projectionRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.projectionRepository = projectionRepository;
    }

    @Transactional
    public Projection createProjection(Long userId, Long courseId, @Valid StringRequestDTO projectionName){
        validateCourse(userId, courseId);
        var course = courseRepository.findByUserIdAndId(userId,courseId).orElseThrow();
        if (projectionRepository.existsByCourseAndName(course,projectionName.string())) {
            throw new DataIntegrityException(projectionName.string());
        }
        var projection = new Projection(course,projectionName.string());
        projectionRepository.save(projection);
        assessmentService.createAssessment(courseId,projection.getId(),course.getAverageMethod());
        return projection;
    }

    public List<Projection> listProjection(Long userId, Long courseId) {
        validateCourse(userId,courseId);
        Course course = courseRepository.findById(courseId).orElseThrow();
        return projectionRepository.findByCourseId(course);
    }

    @Transactional
    public Projection updateProjectionName(Long userId, Long courseId, Long id, StringRequestDTO newProjectionName) {
        validateCourse(userId, courseId);
        var course = courseRepository.findById(courseId).orElseThrow();
        if (projectionRepository.existsByCourseAndName(course,newProjectionName.string()))
            throw new DataIntegrityException(newProjectionName.string());
        var projection = projectionRepository.findByCourseAndId(course,id).orElseThrow
                (()-> new NotFoundArgumentException("Projection id "+id+" not found for the course id "+courseId));
        projection.setName(newProjectionName.string());
        return projection;
    }

    public Projection deleteProjection(Long userId, Long courseId, Long id) {
        validateCourse(userId,courseId);
        var course = courseRepository.findById(courseId).orElseThrow();
        var projection = projectionRepository.findByCourseAndId(course,id).orElseThrow
                (()-> new NotFoundArgumentException("Projection id "+id+" not found for the course id "+courseId));
        projectionRepository.deleteById(id);
        return projection;
    }

    @Transactional
    public void deleteAllProjections(Long userId, Long courseId) {
        validateCourse(userId, courseId);
        var course = courseRepository.findById(courseId).orElseThrow();
        projectionRepository.deleteAllByCourse(course);
    }

    private void validateCourse(Long userId, Long courseId){
        if(!userRepository.existsById(userId))throw new NotFoundArgumentException("User id "+userId+" not found");
        else if(!courseRepository.existsByUserIdAndId(userId,courseId))
            throw new NotFoundArgumentException("Course id "+courseId+" not found for the user id "+userId);
    }
}

