package br.com.gustavohenrique.MediasAPI.controller.rest.v1;

import br.com.gustavohenrique.MediasAPI.controller.rest.v1.mapper.MapDTO;
import br.com.gustavohenrique.MediasAPI.controller.rest.v1.ProjectionController;
import br.com.gustavohenrique.MediasAPI.dtos.AssessmentDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ProjectionDTO;
import br.com.gustavohenrique.MediasAPI.dtos.StringRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Course;
import br.com.gustavohenrique.MediasAPI.model.Projection;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.ProjectionService;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.UserService;
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

@WebMvcTest(ProjectionController.class)
class ProjectionControllerTest {

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
    private ProjectionService projectionService;

    @MockBean
    private UserService userService;

    @MockBean
    private MapDTO mapDTO;

    @Autowired
    private ObjectMapper objectMapper;

    private Projection projection;
    private StringRequestDTO stringRequestDTO;
    private ProjectionDTO projectionDTO;

    @BeforeEach
    void setUp() {
        Users user = new Users(1L, "Gustavo", "gustavo@test.com", new ArrayList<>(), "password", null);
        Course course = new Course(1L, user, "Math", new ArrayList<>(), "P1+P2", 6.0);
        projection = new Projection(1L, course, new ArrayList<>(), "Projection 1", 0.0);
        stringRequestDTO = new StringRequestDTO("Projection 1");
        projectionDTO = new ProjectionDTO(1L, "Projection 1", new ArrayList<AssessmentDTO>(), 0.0);
    }

    @Test
    @DisplayName("Should create a projection")
    @WithMockUser
    void testCreateProjection() throws Exception {
        doNothing().when(projectionService).getAuthenticatedUserByCourseId(1L);
        when(projectionService.createProjection(any(Long.class), any(StringRequestDTO.class))).thenReturn(projection);
        when(mapDTO.projectionDTO(any(Projection.class))).thenReturn(projectionDTO);

        mockMvc.perform(post("/api/v1/1/projections").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stringRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Projection 1"));
    }

    @Test
    @DisplayName("Should return a list of projections")
    @WithMockUser
    void testShowProjections() throws Exception {
        doNothing().when(projectionService).getAuthenticatedUserByCourseId(1L);
        when(projectionService.listProjection(1L)).thenReturn(List.of(projection));
        when(mapDTO.projectionDTO(any(Projection.class))).thenReturn(projectionDTO);

        mockMvc.perform(get("/api/v1/1/projections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Projection 1"));
    }

    @Test
    @DisplayName("Should update projection name")
    @WithMockUser
    void testUpdateProjectionName() throws Exception {
        doNothing().when(projectionService).getAuthenticatedUserByCourseId(1L);
        when(projectionService.updateProjectionName(any(Long.class), any(Long.class), any(StringRequestDTO.class))).thenReturn(projection);

        mockMvc.perform(patch("/api/v1/1/projections/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StringRequestDTO("New Name"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete a projection")
    @WithMockUser
    void testDeleteProjection() throws Exception {
        doNothing().when(projectionService).getAuthenticatedUserByCourseId(1L);
        when(projectionService.deleteProjection(any(Long.class), any(Long.class))).thenReturn(projection);

        mockMvc.perform(delete("/api/v1/1/projections/1").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should delete all projections")
    @WithMockUser
    void testDeleteAllProjections() throws Exception {
        doNothing().when(projectionService).getAuthenticatedUserByCourseId(1L);
        when(userService.getAuthenticatedUser()).thenReturn(new Users());
        doNothing().when(projectionService).deleteAllProjections(any(Long.class), any(Long.class));

        mockMvc.perform(delete("/api/v1/1/projections/all").with(csrf()))
                .andExpect(status().isOk());
    }
}
