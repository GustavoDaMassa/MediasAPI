package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.model.dtos.CourseDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.service.CourseService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("{userId}/courses")
public class CourseController {

    @Autowired
    private ModelMapper modelMapper;

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@PathVariable Long userId, @RequestBody @Valid Course course){
           return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(courseService.createCourse(userId ,course), CourseDTO.class));
    }

    @GetMapping
    public  ResponseEntity<List<CourseDTO>> showCourses(@PathVariable Long userId){
          return ResponseEntity.ok(courseService.listCourses(userId).stream().map(course -> modelMapper.map(course,CourseDTO.class)).collect(Collectors.toList()));
    }

    @PatchMapping("{id}/name")
    public ResponseEntity<CourseDTO> updateCourseName(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto){
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseName(userId, id, nameDto), CourseDTO.class));
    }

    @PatchMapping("{id}/method")
    public ResponseEntity<CourseDTO> updateCourseMethod(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid StringRequestDTO averageMethodDto){
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseAverageMethod(userId, id, averageMethodDto), CourseDTO.class));
    }

    @PatchMapping("{id}/cutoffgrade")
    public ResponseEntity<CourseDTO> updateCourseCutOffGrade(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid DoubleRequestDTO cutOffGradeDto){
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseCutOffGrade(userId, id, cutOffGradeDto), CourseDTO.class));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable Long userId, @PathVariable Long id){
            return ResponseEntity.ok(modelMapper.map(courseService.deleteCourse(userId, id), CourseDTO.class));
    }
}
