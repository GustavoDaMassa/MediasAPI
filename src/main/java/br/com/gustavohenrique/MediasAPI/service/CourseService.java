package br.com.gustavohenrique.MediasAPI.service;

import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import com.sun.source.tree.WhileLoopTree;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Course createCourse(Long userId, @Valid Course course){
        validateUser(userId);
        if(courseRepository.existsByUserIdAndName(userId,course.getName()))throw new DataIntegrityException(course.getName());
        course.setUserId(userId);
        courseRepository.save(course);
        projectionService.createProjection(userId, course.getId(), new StringRequestDTO(course.getName()));
        return courseRepository.save(course);
    }

    public List<Course> listCourses(Long userId) {
        validateUser(userId);
        return courseRepository.findByUserId(userId);
    }

    public Course updateCourseName(Long userId, Long id, @Valid StringRequestDTO nameDto) {
        validateUser(userId);
        if (courseRepository.existsByUserIdAndName(userId,nameDto.string()))throw new DataIntegrityException(nameDto.string());
        var course = courseRepository.findByUserIdAndId(userId,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+ id+" not found for UserId "+userId));
        course.setName(nameDto.string());
        return courseRepository.save(course);
    }

    public Course updateCourseAverageMethod(Long userId, Long id, @Valid StringRequestDTO averageMethodDto){
        validateUser(userId);
        var course = courseRepository.findByUserIdAndId(userId,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+ id+" not found for UserId "+userId));
        projectionService.deleteAllProjections(userId, course.getId());
        course.setAverageMethod(averageMethodDto.string());
        courseRepository.save(course);
        projectionService.createProjection(userId, course.getId(), new StringRequestDTO(course.getName()));
        return course;
    }
    public Course updateCourseCutOffGrade(Long userId, Long id, @Valid DoubleRequestDTO cutOffGradeDto) {
        validateUser(userId);
        var course = courseRepository.findByUserIdAndId(userId,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+ id+" not found for UserId "+userId));
        course.setCutOffGrade(cutOffGradeDto.value());
        return courseRepository.save(course);
    }
    public Course deleteCourse(Long userId, Long id) {
        validateUser(userId);
        var course  = courseRepository.findByUserIdAndId(userId,id)
                .orElseThrow(() -> new NotFoundArgumentException("Course id "+id+" not found for UserId "+userId));
        projectionService.deleteAllProjections(userId, course.getId());
        courseRepository.deleteById(id);
        return course;
    }


    private void validateUser(Long userId) {
        if(!userRepository.existsById(userId))throw new NotFoundArgumentException("User id "+ userId +" not found");
    }
}
