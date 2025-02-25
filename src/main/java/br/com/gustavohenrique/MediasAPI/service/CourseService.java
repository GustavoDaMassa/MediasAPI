package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.controller.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.controller.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private ProjectionService projectionService;

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Course createCourse(Long userId, @Valid Course course) throws Exception {
        validateUser(userId);
        course.setUser(userId);
        courseRepository.save(course);
        projectionService.createProjection(userId, course.getId(), new StringRequestDTO(course.getName()));
        return courseRepository.save(course);
    }

    public List<Course> listCourses(Long userId) {
        validateUser(userId);
        return courseRepository.findByUserId(userId);
    }

    public Course updateCourse(Long userId, Long id, Course newCourse) {
        validateUser(userId);
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setName(newCourse.getName());
        course.setAverageMethod(newCourse.getAverageMethod());
        course.setCutOffGrade(newCourse.getCutOffGrade());
        return courseRepository.save(course);
    }

    public Course updateCourseName(Long userId, Long id, @Valid StringRequestDTO nameDto) {
        validateUser(userId);

        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setName(nameDto.string());
        return courseRepository.save(course);
    }

    public Course updateCourseAverageMethod(Long userId, Long id, @Valid StringRequestDTO averageMethodDto) {
        validateUser(userId);
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setAverageMethod(averageMethodDto.string());
        return courseRepository.save(course);
    }
    public Course updateCourseCutOffGrade(Long userId, Long id, @Valid DoubleRequestDTO cutOffGradeDto) {
        validateUser(userId);
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setCutOffGrade(cutOffGradeDto.value());
        return courseRepository.save(course);
    }
    public Course deleteCourse(Long userId, Long id) {
        validateUser(userId);
        var course  = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course Id "+id+" not found"));
        courseRepository.deleteById(id);
        return course;
    }

    private void validateUser(Long userId) {
        if(userRepository.existsById(userId)) return;
        else throw new IllegalArgumentException("User id "+ userId +" not found");
    }
}
