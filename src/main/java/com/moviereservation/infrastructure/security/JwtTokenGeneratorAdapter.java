package com.moviereservation.infrastructure.security;

import com.moviereservation.application.port.out.TokenGeneratorPort;
import com.moviereservation.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenGeneratorAdapter implements TokenGeneratorPort {

    private final JwtService jwtService;

    @Override
    public String generate(Long userId, String email, String role) {
        return jwtService.generateToken(userId, email, role);
    }
}
