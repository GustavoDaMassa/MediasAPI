package br.com.gustavohenrique.MediasAPI.authentication;

import br.com.gustavohenrique.MediasAPI.model.RefreshToken;
import br.com.gustavohenrique.MediasAPI.model.Role;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenService refreshTokenService;
    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);
        user = new Users(1L, "Gustavo", "gustavo@test.com", null, "password", Role.USER);
    }

    @Test
    @DisplayName("generate - Should create and persist a refresh token with 7-day TTL")
    void generateSuccessfully() {
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(inv -> inv.getArgument(0));

        RefreshToken result = refreshTokenService.generate(user);

        assertNotNull(result.getToken());
        assertEquals(36, result.getToken().length());
        assertFalse(result.isRevoked());
        assertTrue(result.getExpiresAt().isAfter(Instant.now().plus(6, ChronoUnit.DAYS)));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("validate - Should return token when valid and not expired")
    void validateSuccessfully() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(token, user, Instant.now().plus(7, ChronoUnit.DAYS));
        when(refreshTokenRepository.findByTokenAndRevokedFalse(token)).thenReturn(Optional.of(refreshToken));

        RefreshToken result = refreshTokenService.validate(token);

        assertEquals(token, result.getToken());
    }

    @Test
    @DisplayName("validate - Should throw when token not found or revoked")
    void validateThrowsWhenNotFound() {
        when(refreshTokenRepository.findByTokenAndRevokedFalse("invalid")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.validate("invalid"));
    }

    @Test
    @DisplayName("validate - Should throw and revoke when token is expired")
    void validateThrowsWhenExpired() {
        String token = UUID.randomUUID().toString();
        RefreshToken expiredToken = new RefreshToken(token, user, Instant.now().minus(1, ChronoUnit.HOURS));
        when(refreshTokenRepository.findByTokenAndRevokedFalse(token)).thenReturn(Optional.of(expiredToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(expiredToken);

        assertThrows(IllegalArgumentException.class, () -> refreshTokenService.validate(token));
        assertTrue(expiredToken.isRevoked());
    }

    @Test
    @DisplayName("revoke - Should mark token as revoked")
    void revokeSuccessfully() {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(token, user, Instant.now().plus(7, ChronoUnit.DAYS));
        when(refreshTokenRepository.findByTokenAndRevokedFalse(token)).thenReturn(Optional.of(refreshToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        refreshTokenService.revoke(token);

        assertTrue(refreshToken.isRevoked());
        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    @DisplayName("revoke - Should do nothing when token not found")
    void revokeDoesNothingWhenNotFound() {
        when(refreshTokenRepository.findByTokenAndRevokedFalse("nonexistent")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> refreshTokenService.revoke("nonexistent"));
        verify(refreshTokenRepository, never()).save(any());
    }
}
