package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface CourseService {
    Course createCourse(Long userId, @Valid RequestCourseDto course);

    List<Course> listCourses(Long userId);

    Course updateCourseName(Long userId, Long id, @Valid StringRequestDTO nameDto);

    Course updateCourseAverageMethod(Long userId, Long id, @Valid StringRequestDTO averageMethodDto);

    Course updateCourseCutOffGrade(Long userId, Long id, @Valid DoubleRequestDTO cutOffGradeDto);

    Course deleteCourse(Long userId, Long id);

    void getAuthenticatedUser(Long userId);
}
