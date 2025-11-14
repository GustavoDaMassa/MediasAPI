package br.com.gustavohenrique.MediasAPI.controller.rest.v1;

import br.com.gustavohenrique.MediasAPI.controller.rest.v1.AssessmentController;
import br.com.gustavohenrique.MediasAPI.dtos.DoubleRequestDTO;
import br.com.gustavohenrique.MediasAPI.model.Assessment;
import br.com.gustavohenrique.MediasAPI.service.Interfaces.AssessmentService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssessmentController.class)
class AssessmentControllerTest {

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
    private AssessmentService assessmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Assessment assessment;
    private DoubleRequestDTO doubleRequestDTO;

    @BeforeEach
    void setUp() {
        assessment = new Assessment("P1", 8.0, 10.0, null);
        doubleRequestDTO = new DoubleRequestDTO(8.0);
    }

    @Test
    @DisplayName("Should insert a grade in an assessment")
    @WithMockUser
    void testInsertGrade() throws Exception {
        doNothing().when(assessmentService).getAuthenticatedUserByProjectionId(1L);
        when(assessmentService.insertGrade(any(Long.class), any(Long.class), any(DoubleRequestDTO.class))).thenReturn(assessment);

        mockMvc.perform(patch("/api/v1/1/assessments/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doubleRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return a list of assessments")
    @WithMockUser
    void testShowAssessment() throws Exception {
        doNothing().when(assessmentService).getAuthenticatedUserByProjectionId(1L);
        when(assessmentService.listAssessment(1L)).thenReturn(List.of(assessment));

        mockMvc.perform(get("/api/v1/1/assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].identifier").value("P1"));
    }
}
