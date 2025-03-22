package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final ProjectionService projectionService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    @Autowired
    public CourseServiceImpl(ProjectionService projectionService, CourseRepository courseRepository,
                             UserRepository userRepository) {
        this.projectionService = projectionService;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Course createCourse(Long userId, @Valid RequestCourseDto courseDto){
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        if(courseRepository.existsByUserAndName(user,courseDto.name()))throw new DataIntegrityException(courseDto.name());
        var course = new Course(null,
                user, courseDto.name(), null, courseDto.averageMethod(), courseDto.cutOffGrade());
        courseRepository.save(course);
        projectionService.createProjection(course.getId(), new StringRequestDTO(course.getName()));
        return courseRepository.save(course);
    }

    public List<Course> listCourses(Long userId) {
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        return courseRepository.findByUser(user);
    }
    @Transactional
    public Course updateCourseName(Long userId, Long id, @Valid StringRequestDTO nameDto) {
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        if (courseRepository.existsByUserAndName(user,nameDto.string()))throw new DataIntegrityException(nameDto.string());
        var course = courseRepository.findByUserAndId(user,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+ id+" not found for UserId "+userId));
        course.setName(nameDto.string());
        return courseRepository.save(course);
    }
    @Transactional
    public Course updateCourseAverageMethod(Long userId, Long id, @Valid StringRequestDTO averageMethodDto){
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        var course = courseRepository.findByUserAndId(user,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+ id+" not found for UserId "+userId));
        projectionService.deleteAllProjections(course.getId());
        course.setAverageMethod(averageMethodDto.string());
        courseRepository.save(course);
        projectionService.createProjection(course.getId(), new StringRequestDTO(course.getName()));
        return course;
    }
    @Transactional
    public Course updateCourseCutOffGrade(Long userId, Long id, @Valid DoubleRequestDTO cutOffGradeDto) {
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        var course = courseRepository.findByUserAndId(user,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+ id+" not found for UserId "+userId));
        course.setCutOffGrade(cutOffGradeDto.value());
        return courseRepository.save(course);
    }

    @Transactional
    public Course deleteCourse(Long userId, Long id) {
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        var course  = courseRepository.findByUserAndId(user,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+id+" not found for UserId "+userId));
        courseRepository.deleteCourse(id);
        return course;
    }


    private void validateUser(Long userId) {
        if(!userRepository.existsById(userId))throw new NotFoundArgumentException("User id "+ userId +" not found");
    }
}
