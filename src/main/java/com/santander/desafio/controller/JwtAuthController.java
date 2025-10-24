package com.santander.desafio.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class JwtAuthController {

    private final AuthenticationManager authManager;
    private final JwtEncoder encoder;

    public JwtAuthController(AuthenticationManager authManager, JwtEncoder encoder) {
        this.authManager = authManager;
        this.encoder = encoder;
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            Instant now = Instant.now();
            long expiresIn = 3600; // 1h
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("desafio-santander")
                    .issuedAt(now)
                    .expiresAt(now.plus(expiresIn, ChronoUnit.SECONDS))
                    .subject(authentication.getName())
                    .claim("scope", "USER")
                    .build();
            String tokenValue = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            return ResponseEntity.ok(Map.of(
                    "access_token", tokenValue,
                    "token_type", "Bearer",
                    "expires_in", expiresIn
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
        }
    }
}
