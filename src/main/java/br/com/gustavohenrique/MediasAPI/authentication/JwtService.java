package br.com.gustavohenrique.MediasAPI.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private final JwtEncoder encoder;

    @Autowired
    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication){
        Instant now = Instant.now();
        long time = 16000L;

        String scopes = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        // Extrai a role do Authentication object
        String role = authentication.getAuthorities().stream()
                .filter(a -> a.getAuthority().startsWith("ROLE_"))
                .map(a -> a.getAuthority().substring("ROLE_".length()))
                .findFirst()
                .orElse("USER");

        var claims = JwtClaimsSet.builder().issuer("medias-api")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(time))
                .subject(authentication.getName())
                .claim("Scopes",scopes)
                .claim("roles", Collections.singletonList(role))
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
