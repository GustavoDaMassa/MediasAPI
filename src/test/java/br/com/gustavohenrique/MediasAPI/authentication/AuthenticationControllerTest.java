package br.com.gustavohenrique.MediasAPI.authentication;

import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import br.com.gustavohenrique.MediasAPI.dtos.RefreshRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.TokenResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /authenticate - Should return TokenResponseDTO on success")
    void authenticateSuccessfully() throws Exception {
        AuthDto authDto = new AuthDto();
        authDto.setEmail("user@test.com");
        authDto.setPassword("password");

        TokenResponseDTO tokenResponse = new TokenResponseDTO("access-token", "Bearer", 900L, "refresh-token");
        when(authenticationService.authenticate(any(AuthDto.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(900))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    @DisplayName("POST /authenticate/refresh - Should return new TokenResponseDTO")
    void refreshSuccessfully() throws Exception {
        RefreshRequestDTO request = new RefreshRequestDTO("old-refresh-token");
        TokenResponseDTO tokenResponse = new TokenResponseDTO("new-access-token", "Bearer", 900L, "new-refresh-token");
        when(authenticationService.refresh(any(RefreshRequestDTO.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/authenticate/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @Test
    @DisplayName("POST /authenticate/logout - Should return 204 No Content")
    void logoutSuccessfully() throws Exception {
        RefreshRequestDTO request = new RefreshRequestDTO("refresh-token");
        doNothing().when(authenticationService).logout(any(RefreshRequestDTO.class));

        mockMvc.perform(post("/authenticate/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
