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
    public ResponseEntity<?> createCourse(@PathVariable Long userId, @RequestBody @Valid Course course){
       try {
           return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(courseService.createCourse(userId ,course), CourseDTO.class));
       } catch ( IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @GetMapping
    public  ResponseEntity<?> showCourses(@PathVariable Long userId){
      try {
          return ResponseEntity.ok(courseService.listCourses(userId).stream().map(course -> modelMapper.map(course,CourseDTO.class)).collect(Collectors.toList()));
      }catch ( IllegalArgumentException e){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid Course newCourse){
        try {
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourse(userId, id, newCourse), CourseDTO.class));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{id}/name")
    public ResponseEntity<?> updateCourseName(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto){
        try {
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseName(userId, id, nameDto), CourseDTO.class));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PatchMapping("{id}/method")
    public ResponseEntity<?> updateCourseMethod(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid StringRequestDTO averageMethodDto){
        try {
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseAverageMethod(userId, id, averageMethodDto), CourseDTO.class));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{id}/cutoffgrade")
    public ResponseEntity<?> updateCourseCutOffGrade(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid DoubleRequestDTO cutOffGradeDto){
        try {
            return ResponseEntity.ok(modelMapper.map(courseService.updateCourseCutOffGrade(userId, id, cutOffGradeDto), CourseDTO.class));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long userId, @PathVariable Long id){
        try {
            return ResponseEntity.ok(modelMapper.map(courseService.deleteCourse(userId, id), CourseDTO.class));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
