package br.com.gustavohenrique.MediasAPI.authentication;

import br.com.gustavohenrique.MediasAPI.dtos.AuthDto;
import br.com.gustavohenrique.MediasAPI.dtos.RefreshRequestDTO;
import br.com.gustavohenrique.MediasAPI.dtos.TokenResponseDTO;
import br.com.gustavohenrique.MediasAPI.model.RefreshToken;
import br.com.gustavohenrique.MediasAPI.model.Users;
import br.com.gustavohenrique.MediasAPI.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthenticationService(JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 RefreshTokenService refreshTokenService,
                                 UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    public TokenResponseDTO authenticate(AuthDto authDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword())
        );

        Users user = userRepository.findByEmail(authDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + authDto.getEmail()));

        String accessToken = jwtService.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.generate(user);

        return new TokenResponseDTO(
                accessToken,
                "Bearer",
                JwtService.ACCESS_TOKEN_TTL,
                refreshToken.getToken()
        );
    }

    public TokenResponseDTO refresh(RefreshRequestDTO request) {
        RefreshToken refreshToken = refreshTokenService.validate(request.refreshToken());
        refreshTokenService.revoke(request.refreshToken());

        Users user = refreshToken.getUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null,
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(), "", java.util.List.of(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )).getAuthorities()
        );

        String newAccessToken = jwtService.generateToken(authentication);
        RefreshToken newRefreshToken = refreshTokenService.generate(user);

        return new TokenResponseDTO(
                newAccessToken,
                "Bearer",
                JwtService.ACCESS_TOKEN_TTL,
                newRefreshToken.getToken()
        );
    }

    public void logout(RefreshRequestDTO request) {
        refreshTokenService.revoke(request.refreshToken());
    }
}
