package br.com.gustavohenrique.MediasAPI.service.Interfaces;

import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService extends OwnershipValidator {
    Course createCourse(Long userId, @Valid RequestCourseDto course);

    Page<Course> listCourses(Long userId, Pageable pageable);

    Course updateCourseName(Long userId, Long id, @Valid StringRequestDTO nameDto);

    Course updateCourseAverageMethod(Long userId, Long id, @Valid StringRequestDTO averageMethodDto);

    Course updateCourseCutOffGrade(Long userId, Long id, @Valid DoubleRequestDTO cutOffGradeDto);

    Course deleteCourse(Long userId, Long id);

}
