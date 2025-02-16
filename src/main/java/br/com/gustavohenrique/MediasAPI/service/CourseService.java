package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.controller.dtos.DoubleUpdateDTO;
import br.com.gustavohenrique.MediasAPI.controller.dtos.StringUpdateDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.User;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public Course createCourse(Long userId, @Valid Course course) {
        validateUser(userId);
        course.setUser(userId);
        return courseRepository.save(course);
    }

    public List<Course> listCourses(Long userId) {
        validateUser(userId);
        return courseRepository.findByUserId(userId);
    }

    private void validateUser(Long userId) {
        if(userRepository.existsById(userId)) return;
        else throw new IllegalArgumentException("User id "+ userId +" not found");
    }

    public Course updateCourse(Long userId, Long id, Course newCourse) {
        validateUser(userId);
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setName(newCourse.getName());
        course.setAverageMethod(newCourse.getAverageMethod());
        course.setCutOffGrade(newCourse.getCutOffGrade());
        return courseRepository.save(course);
    }

    public Course updateCourseName(Long userId, Long id, @Valid StringUpdateDTO nameDto) {
        validateUser(userId);

        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setName(nameDto.newString());
        return courseRepository.save(course);
    }
    public Course updateCourseAverageMethod(Long userId, Long id, @Valid StringUpdateDTO averageMethodDto) {
        validateUser(userId);
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setAverageMethod(averageMethodDto.newString());
        return courseRepository.save(course);
    }
    public Course updateCourseCutOffGrade(Long userId, Long id, @Valid DoubleUpdateDTO cutOffGradeDto) {
        validateUser(userId);
        var course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course id "+ id+" not found"));
        course.setCutOffGrade(cutOffGradeDto.newValue());
        return courseRepository.save(course);
    }

    public Course deleteCourse(Long userId, Long id) {
        validateUser(userId);
        var course  = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Course Id "+id+" not found"));
        courseRepository.deleteById(id);
        return course;
    }
}
