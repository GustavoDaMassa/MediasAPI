package br.com.gustavohenrique.MediasAPI.controller.rest.v1;

import br.com.gustavohenrique.MediasAPI.controller.rest.v1.CourseController;
import br.com.gustavohenrique.MediasAPI.dtos.RequestCourseDto;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.CourseService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private ProjectionService projectionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Course course;
    private Users user;
    private RequestCourseDto requestCourseDto;

    @BeforeEach
    void setUp() {
        user = new Users(1L, "Gustavo", "gustavo@test.com", new ArrayList<>(), "password", null);
        course = new Course(1L, user, "Math", new ArrayList<>(), "P1+P2", 6.0);
        requestCourseDto = new RequestCourseDto("Math", "P1+P2", 6.0);
    }

    @Test
    @DisplayName("Should create a course")
    @WithMockUser
    void testCreateCourse() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(courseService.createCourse(any(Long.class), any(RequestCourseDto.class))).thenReturn(course);

        mockMvc.perform(post("/api/v1/1/courses").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCourseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Math"));
    }

    @Test
    @DisplayName("Should return a list of courses")
    @WithMockUser
    void testShowCourses() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(courseService.listCourses(1L)).thenReturn(List.of(course));

        mockMvc.perform(get("/api/v1/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Math"));
    }

    @Test
    @DisplayName("Should update course name")
    @WithMockUser
    void testUpdateCourseName() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(courseService.updateCourseName(any(Long.class), any(Long.class), any(StringRequestDTO.class))).thenReturn(course);

        mockMvc.perform(patch("/api/v1/1/courses/1/name").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StringRequestDTO("New Math"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update course method")
    @WithMockUser
    void testUpdateCourseMethod() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(courseService.updateCourseAverageMethod(any(Long.class), any(Long.class), any(StringRequestDTO.class))).thenReturn(course);

        mockMvc.perform(patch("/api/v1/1/courses/1/method").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StringRequestDTO("P1+P2+P3"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update course cut off grade")
    @WithMockUser
    void testUpdateCourseCutOffGrade() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(courseService.updateCourseCutOffGrade(any(Long.class), any(Long.class), any(DoubleRequestDTO.class))).thenReturn(course);

        mockMvc.perform(patch("/api/v1/1/courses/1/cutoffgrade").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DoubleRequestDTO(7.0))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete a course")
    @WithMockUser
    void testDeleteCourse() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(courseService.deleteCourse(any(Long.class), any(Long.class))).thenReturn(course);

        mockMvc.perform(delete("/api/v1/1/courses/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return a list of projections")
    @WithMockUser
    void testShowAllProjections() throws Exception {
        doNothing().when(courseService).getAuthenticatedUser(1L);
        when(projectionService.listAllProjection(1L)).thenReturn(List.of(new Projection()));

        mockMvc.perform(get("/api/v1/1/courses/projections"))
                .andExpect(status().isOk());
    }
}
