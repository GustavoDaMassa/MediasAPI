package br.com.gustavohenrique.MediasAPI.controller.rest;

import br.com.gustavohenrique.MediasAPI.exception.StandardError;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.model.dtos.CourseDTO;
import br.com.gustavohenrique.MediasAPI.model.dtos.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class CourseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName(" IT Should return an courseDto when create a course successfully")
    void createCourseSuccessfully() throws Exception {
        var course = new Course(null,null,"Matemática","P3+P4",6.0);
        var courseDTO = new CourseDTO(1L,course.getName(),course.getAverageMethod(),6.0);
        mockMvc.perform(post("/1/courses").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(courseDTO.getName()))
                .andExpect(jsonPath("$.averageMethod").value(courseDTO.getAverageMethod()))
                .andExpect(jsonPath("$.cutOffGrade").value(courseDTO.getCutOffGrade()))
                .andExpect(jsonPath("$.userId").doesNotExist())
                .andExpect(jsonPath("$.id").value(courseDTO.getId()));
    }

    @Test
    @DisplayName(" IT Should return an exception when Name already exist")
    void createCourseAlreadyExist() throws Exception {
        var course = new Course(null,null,"Banco de Dados","P3+P4",6.0);
        var exception = new StandardError(400,
                "The attribute Banco de Dados already exist for this context","/1/courses");
        mockMvc.perform(post("/1/courses").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(exception.getStatusCode()))
                .andExpect(jsonPath("$.error").value(exception.getError()))
                .andExpect(jsonPath("$.path").value(exception.getPath()));
    }

    @Test
    void showCourses() {
    }

    @Test
    void updateCourseName() {
    }

    @Test
    void updateCourseMethod() {
    }

    @Test
    void updateCourseCutOffGrade() {
    }

    @Test
    void deleteCourse() {

    }
}
