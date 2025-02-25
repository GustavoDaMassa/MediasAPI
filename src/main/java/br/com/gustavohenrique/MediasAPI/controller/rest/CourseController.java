package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.controller.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.controller.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("{userId}/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@PathVariable Long userId, @RequestBody @Valid Course course){
       try {
           return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(userId ,course));
       } catch ( IllegalArgumentException e){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @GetMapping
    public  ResponseEntity<?> showCourses(@PathVariable Long userId){
      try {
          return ResponseEntity.ok(courseService.listCourses(userId));
      }catch ( IllegalArgumentException e){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
      }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid Course newCourse){
        try {
            return ResponseEntity.ok(courseService.updateCourse(userId, id, newCourse));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{id}/name")
    public ResponseEntity<?> updateCourseName(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid StringRequestDTO nameDto){
        try {
            return ResponseEntity.ok(courseService.updateCourseName(userId, id, nameDto));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PatchMapping("{id}/method")
    public ResponseEntity<?> updateCourseMethod(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid StringRequestDTO averageMethodDto){
        try {
            return ResponseEntity.ok(courseService.updateCourseAverageMethod(userId, id, averageMethodDto));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("{id}/cutoffgrade")
    public ResponseEntity<?> updateCourseCutOffGrade(@PathVariable Long userId ,@PathVariable Long id, @RequestBody @Valid DoubleRequestDTO cutOffGradeDto){
        try {
            return ResponseEntity.ok(courseService.updateCourseCutOffGrade(userId, id, cutOffGradeDto));
        } catch ( IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long userId, @PathVariable Long id){
        try {
            return ResponseEntity.ok(courseService.deleteCourse(userId, id));
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
