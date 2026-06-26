package br.com.gustavohenrique.MediasAPI.service.Impl;

import br.com.gustavohenrique.MediasAPI.authentication.ApiKeyService;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationCreatedDTO;
import br.com.gustavohenrique.MediasAPI.dtos.ApplicationDTO;
import br.com.gustavohenrique.MediasAPI.dtos.CreateApplicationDTO;
import br.com.gustavohenrique.MediasAPI.model.Application;
import br.com.gustavohenrique.MediasAPI.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApiKeyService apiKeyService;

    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        applicationService = new ApplicationServiceImpl(applicationRepository, apiKeyService);
    }

    @Test
    @DisplayName("create - Should generate key, hash it and persist the application")
    void createSuccessfully() {
        when(apiKeyService.generate()).thenReturn("mapi_abc123");
        when(apiKeyService.hash("mapi_abc123")).thenReturn("hashed");
        when(apiKeyService.extractPrefix("mapi_abc123")).thenReturn("mapi_abc123");
        when(applicationRepository.save(any(Application.class))).thenAnswer(inv -> inv.getArgument(0));

        ApplicationCreatedDTO result = applicationService.create(new CreateApplicationDTO("MyApp", "desc"));

        assertEquals("MyApp", result.name());
        assertEquals("mapi_abc123", result.apiKey());
        assertTrue(result.active());
        assertEquals(60, result.rateLimitPerMinute());
        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    @DisplayName("toDTO - Should map Application to ApplicationDTO without exposing key")
    void toDTOCorrectly() {
        Application app = new Application("MyApp", "desc", "hashed", "mapi_abc");

        ApplicationDTO dto = applicationService.toDTO(app);

        assertEquals("MyApp", dto.name());
        assertEquals("mapi_abc", dto.apiKeyPrefix());
        assertTrue(dto.active());
    }

    @Test
    @DisplayName("revoke - Should set active to false and save")
    void revokeSuccessfully() {
        Application app = new Application("MyApp", null, "hashed", "mapi_abc");
        when(applicationRepository.save(any(Application.class))).thenReturn(app);

        applicationService.revoke(app);

        assertFalse(app.isActive());
        verify(applicationRepository).save(app);
    }

    @Test
    @DisplayName("rotateKey - Should generate new key, update hash and return new DTO")
    void rotateKeySuccessfully() {
        Application app = new Application("MyApp", null, "old-hash", "mapi_old");
        when(apiKeyService.generate()).thenReturn("mapi_new123");
        when(apiKeyService.hash("mapi_new123")).thenReturn("new-hash");
        when(apiKeyService.extractPrefix("mapi_new123")).thenReturn("mapi_new12");
        when(applicationRepository.save(any(Application.class))).thenReturn(app);

        ApplicationCreatedDTO result = applicationService.rotateKey(app);

        assertEquals("mapi_new123", result.apiKey());
        assertEquals("new-hash", app.getApiKeyHash());
        verify(applicationRepository).save(app);
    }
}
