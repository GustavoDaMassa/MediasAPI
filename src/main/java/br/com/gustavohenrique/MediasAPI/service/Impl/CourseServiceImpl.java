package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.CourseNotFoundException;
import br.com.gustavohenrique.MediasAPI.exception.UserNotFoundException;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import br.com.gustavohenrique.MediasAPI.service.OwnershipValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl extends OwnedResourceService implements CourseService {

    private final ProjectionService projectionService;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseServiceImpl(ProjectionService projectionService, CourseRepository courseRepository,
                             UserRepository userRepository, OwnershipValidationService ownershipValidationService) {
        super(ownershipValidationService);
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
                .orElseThrow(() -> new CourseNotFoundException(id, userId));
        course.setName(nameDto.string());
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourseAverageMethod(Long userId, Long id, @Valid StringRequestDTO averageMethodDto){
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        var course = courseRepository.findByUserAndId(user,id)
                .orElseThrow(() -> new CourseNotFoundException(id, userId));
        projectionService.deleteAllProjections(course.getId(), userId);
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
                .orElseThrow(() -> new CourseNotFoundException(id, userId));
        course.setCutOffGrade(cutOffGradeDto.value());
        return courseRepository.save(course);
    }

    @Transactional
    public Course deleteCourse(Long userId, Long id) {
        validateUser(userId);
        var user = userRepository.findById(userId).orElseThrow();
        var course  = courseRepository.findByUserAndId(user,id)
                .orElseThrow(() -> new CourseNotFoundException(id, userId));
        courseRepository.deleteCourse(id, userId);
        return course;
    }

    private void validateUser(Long userId) {
        if(!userRepository.existsById(userId))throw new UserNotFoundException(userId);
    }

    @Override
    protected Long resolveOwnerId(Long userId) {
        return userId;
    }
}
