package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.controller.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.ProjectionRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectionService {

    @Autowired
    private AssessmentService assessmentService;

    private final UserRepository userRepository;
    private  final CourseRepository courseRepository;
    private  final ProjectionRepository projectionRepository;

    public ProjectionService(UserRepository userRepository, CourseRepository courseRepository, ProjectionRepository projectionRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.projectionRepository = projectionRepository;
    }

    public Projection createProjection(Long userId, Long courseId, @Valid StringRequestDTO projectionName) throws Exception {
        validateCourse(userId, courseId);
        var projection = new Projection(courseId,projectionName.string());
        var course = courseRepository.findById(courseId).orElseThrow();
        projectionRepository.save(projection);
        assessmentService.createAssessment(projection.getId(),course.getAverageMethod());
        return projection;
    }



    public List<Projection> listProjection(Long userId, Long courseId) {
        validateCourse(userId,courseId);
        return projectionRepository.findByCourseId(courseId);
    }

    public Projection updateProjectionName(Long userId, Long courseId, Long id, StringRequestDTO newProjectionName) {
        validateCourse(userId, courseId);
        var projection = projectionRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Projection id "+id+" not found"));
        projection.setName(newProjectionName.string());
        return projection;
    }

    public Projection deleteProjection(Long userId, Long courseId, Long id) {
        validateCourse(userId,courseId);
        var projection = projectionRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Projection id "+id+" not found"));
        projectionRepository.deleteById(id);
        return projection;
    }

    public void deleteAllProjections(Long userId, Long courseId) {
        validateCourse(userId, courseId);
        projectionRepository.deleteAll();
    }

    private void validateCourse(Long userId, Long courseId){
        if(!userRepository.existsById(userId))throw new IllegalArgumentException("User id "+userId+" not found");
        else if(!courseRepository.existsById(courseId)) throw new IllegalArgumentException("Course id "+courseId+" not found");
    }



    private void createAssessmennt(String method) {
    }
}

