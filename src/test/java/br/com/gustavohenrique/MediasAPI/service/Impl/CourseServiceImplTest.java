package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.exception.DataIntegrityException;
import br.com.gustavohenrique.MediasAPI.exception.NotFoundArgumentException;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.Role;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.CourseRepository;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Autowired
    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private ProjectionServiceImpl projectionService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    private final Long userId = 1L;
    private final Users user = new Users(userId,"Gustavo","gustavo.pereira@discente.ufg.br",
            new ArrayList<>(),"Senha criptografada", Role.USER);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("createCourse - Should create a course successfully")
    void createCourseSuccessfully() {
        var courseDto = new RequestCourseDto("SGBD","P1+P2",6.0);
        var course = new Course(null,null,courseDto.name(),null,courseDto.averageMethod(),
                courseDto.cutOffGrade());

        when(courseRepository.existsByUserAndName(eq(user),eq("SGBD"))).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(projectionService.createProjection(eq(course.getId()),any(StringRequestDTO.class)))
                .thenReturn(new Projection());

        var response = courseService.createCourse(userId,courseDto);

        AssertCourse(course, response);
        verify(userRepository).findById(userId);
        verify(courseRepository).existsByUserAndName(any(Users.class),eq(courseDto.name()));
        verify(courseRepository,times(2)).save(any(Course.class));
        verify(projectionService).createProjection(eq(course.getId()), any(StringRequestDTO.class));
    }

    @Test
    @DisplayName("createCourse Should return exeception when the course with the chosen name already exist")
    void createCourseFailureDataIntegrity(){
        var courseDto = new RequestCourseDto("SGBD","P1+P2",6.0);

        when(courseRepository.existsByUserAndName(eq(user),eq(courseDto.name()))).thenThrow(DataIntegrityException.class);

        assertThrows(DataIntegrityException.class, ()-> courseService.createCourse(userId,courseDto));

        verify(userRepository).findById(userId);
        verify(courseRepository,never()).save(any(Course.class));
        verify(projectionService,never()).createProjection(anyLong(),any(StringRequestDTO.class));
    }

    @Test
    @DisplayName("listCourses - Should return a list of course successfully")
    void listCoursesSuccessfully() {
        var course1 = new Course(1L,user,"BD1",null,"p1+p2",6);
        var course2 = new Course(2L,user,"BD2",null,"p1+p2+p3",6);

        when(courseRepository.findByUser(user)).thenReturn(List.of(course1,course2));

        var courses = courseService.listCourses(userId);

        AssertCourse(course1,courses.get(0));
        AssertCourse(course2,courses.get(1));
        verify(courseRepository).findByUser(user);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("updateCourseName - Should return the course name updated successfully")
    void updateCourseNameSuccessfully() {
        var nameDto = new StringRequestDTO("BD");
        var course = new Course(null,null,nameDto.string(),null,"p1+p2",
                6);

        when(courseRepository.existsByUserAndName(eq(user),eq("BD"))).thenReturn(false);
        when(courseRepository.findByUserAndId(eq(user),eq(course.getId()))).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        var response = courseService.updateCourseName(userId,course.getId(),nameDto);

        AssertCourse(course,response);
        verify(courseRepository).findByUserAndId(eq(user),eq(course.getId()));
        verify(courseRepository).existsByUserAndName(eq(user),eq("BD"));
        verify(userRepository).findById(userId);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @DisplayName("updateCourseName - Should return an exception when the course name updated already chosen")
    void updateCourseNameAlreadyExist() {
        var nameDto = new StringRequestDTO("BD");
        var course = new Course(null,null,nameDto.string(),null,"p1+p2",
                6);

        when(courseRepository.existsByUserAndName(eq(user),eq("BD"))).thenThrow(DataIntegrityException.class);

        assertThrows(DataIntegrityException.class, () -> courseService.updateCourseName(userId,course.getId(),nameDto));

        verify(courseRepository,never()).findByUserAndId(eq(user),eq(course.getId()));
        verify(courseRepository).existsByUserAndName(eq(user),eq("BD"));
        verify(userRepository).findById(userId);
        verify(courseRepository,never()).save(any(Course.class));
    }

    @Test
    @DisplayName("updateCourseName - Should return an exception when the course not exist")
    void updateCourseNameNotFound() {
        var nameDto = new StringRequestDTO("BD");
        var course = new Course(null,null,nameDto.string(),null,"p1+p2",
                6);

        when(courseRepository.existsByUserAndName(eq(user),eq("BD"))).thenReturn(false);
        when(courseRepository.findByUserAndId(eq(user),eq(course.getId()))).thenThrow(NotFoundArgumentException.class);


        assertThrows(NotFoundArgumentException.class, () -> courseService.updateCourseName(userId,course.getId(),nameDto));

        verify(userRepository).findById(userId);
        verify(courseRepository).existsByUserAndName(eq(user),eq("BD"));
        verify(courseRepository).findByUserAndId(eq(user),eq(course.getId()));
        verify(courseRepository,never()).save(any(Course.class));
    }

    @Test
    @DisplayName("updateCourseAverageMethod - Should return the course not exist ")
    void updateCourseAverageMethodCourseNotFound() {
        var averageMethodDto = new StringRequestDTO("Atividade + Prova");
        var course = new Course(null,null,"BD",null,averageMethodDto.string(),6);

        when(courseRepository.findByUserAndId(any(Users.class),eq(course.getId()))).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class,
                ()-> courseService.updateCourseAverageMethod(userId,course.getId(),averageMethodDto));

        verify(userRepository).findById(userId);
        verify(courseRepository).findByUserAndId(any(Users.class), eq(course.getId()));
        verify(projectionService,never()).deleteAllProjections(anyLong(), anyLong());
        verify(courseRepository,never()).save(any(Course.class));
        verify(projectionService,never()).createProjection(eq(course.getId()), any(StringRequestDTO.class));

    }

    @Test
    @DisplayName("updateCourseAverageMethod - Should return the course with the Average method redefined")
    void updateCourseAverageMethodSuccessfully() {
        var averageMethodDto = new StringRequestDTO("Atividade + Prova");
        var course = new Course(null,null,"BD",null,averageMethodDto.string(),6);

        when(courseRepository.findByUserAndId(any(Users.class),eq(course.getId()))).thenReturn(Optional.of(course));
        doNothing().when(projectionService).deleteAllProjections(course.getId(), userId);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(projectionService.createProjection(eq(course.getId()), any(StringRequestDTO.class)))
                .thenReturn(new Projection());

        var response = courseService.updateCourseAverageMethod(userId,course.getId(),averageMethodDto);

        AssertCourse(course,response);
        verify(userRepository).findById(userId);
        verify(courseRepository).findByUserAndId(any(Users.class), eq(course.getId()));
        verify(projectionService).deleteAllProjections(course.getId(), userId);
        verify(courseRepository).save(any(Course.class));
        verify(projectionService).createProjection(eq(course.getId()), any(StringRequestDTO.class));
    }

    @Test
    @DisplayName("updateCourseCutOffGrade - Should update the cut off grade successfully")
    void updateCourseCutOffGradeSuccessfully() {
        var cutOffGradeDto = new DoubleRequestDTO(7.0);
        var course = new Course(null,null,"BD",null,"Atividade + Prova",
                cutOffGradeDto.value());

        when(courseRepository.findByUserAndId(any(Users.class),eq(course.getId()))).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        var response = courseService.updateCourseCutOffGrade(userId, course.getId(),cutOffGradeDto);

        AssertCourse(course, response);
        verify(userRepository).findById(userId);
        verify(courseRepository).findByUserAndId(any(Users.class), eq(course.getId()));
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @DisplayName("updateCourseCutOffGrade - Should return an exception when course not exist")
    void updateCourseCutOffGradeCourseNotFound() {
        var cutOffGradeDto = new DoubleRequestDTO(7.0);
        var course = new Course(null,null,"BD",null,"Atividade + Prova",
                cutOffGradeDto.value());

        when(courseRepository.findByUserAndId(any(Users.class),eq(course.getId()))).thenThrow(NotFoundArgumentException.class);


        assertThrows(NotFoundArgumentException.class,
                ()-> courseService.updateCourseCutOffGrade(userId, course.getId(), cutOffGradeDto));

        verify(userRepository).findById(userId);
        verify(courseRepository).findByUserAndId(any(Users.class), eq(course.getId()));
        verify(courseRepository,never()).save(any(Course.class));
    }

    @Test
    @DisplayName("deleteCourse - Should return the course deleted")
    void deleteCourseSuccessfully() {
        var course = new Course();

        when(courseRepository.findByUserAndId(user, course.getId())).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteCourse(course.getId(), userId);

        var response = courseService.deleteCourse(userId, course.getId());

        AssertCourse(course,response);
        verify(userRepository).findById(userId);
        verify(courseRepository).findByUserAndId(user, course.getId());
        verify(courseRepository).deleteCourse(course.getId(), userId);
    }

    @Test
    @DisplayName("deleteCourse - Should return exception when course not exist")
    void deleteCourseNotFound() {
        var course = new Course();

        when(courseRepository.findByUserAndId(user, course.getId())).thenThrow(NotFoundArgumentException.class);

        assertThrows(NotFoundArgumentException.class, () -> courseService.deleteCourse(userId, course.getId()));

        verify(userRepository).findById(userId);
        verify(courseRepository).findByUserAndId(user, course.getId());
        verify(courseRepository,never()).deleteCourse(anyLong(), anyLong());
    }


    @Test
    @DisplayName("All Methods - Should return exception when user id not found for all methods")
    void courseFailureUserNotFound(){
        var courseDto = new RequestCourseDto("SGBD","P1+P2",6.0);

        when(userRepository.findById(userId)).thenThrow(NotFoundArgumentException.class);


        assertThrows(NotFoundArgumentException.class, ()-> courseService.createCourse(userId,courseDto));
        assertThrows(NotFoundArgumentException.class,()->courseService.listCourses(userId));
        assertThrows(NotFoundArgumentException.class,()->courseService.updateCourseName(userId,1l,
                new StringRequestDTO(courseDto.name())));
        assertThrows(NotFoundArgumentException.class,()->courseService.updateCourseAverageMethod(userId,1l,
                    new StringRequestDTO(courseDto.averageMethod())));
        assertThrows(NotFoundArgumentException.class,()->courseService.updateCourseCutOffGrade(userId,1l,
                    new DoubleRequestDTO(courseDto.cutOffGrade())));

        verify(userRepository,times(5)).findById(userId);
        verify(courseRepository,never()).existsByUserAndName(any(Users.class),eq(courseDto.name()));
        verify(courseRepository,never()).save(any(Course.class));
        verify(projectionService,never()).createProjection(anyLong(), any(StringRequestDTO.class));
    }

    private static void AssertCourse(Course course, Course response) {
        assertEquals(course.getId(), response.getId());
        assertEquals(course.getName(), response.getName());
        assertEquals(course.getAverageMethod(), response.getAverageMethod());
        assertEquals(course.getCutOffGrade(), response.getCutOffGrade());
        assertEquals(course.getUser(), response.getUser());
        assertEquals(course.getProjection(), response.getProjection());
        assertEquals(course.getClass(), response.getClass());
    }
}