package com.moviereservation.controller;

import com.moviereservation.application.dto.auth.AuthResponse;
import com.moviereservation.application.dto.auth.LoginRequest;
import com.moviereservation.application.dto.auth.SignupRequest;
import com.moviereservation.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Sign up and login endpoints")
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user")
    public AuthResponse signup(@Valid @RequestBody SignupRequest request) {
        return authUseCase.signup(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authUseCase.login(request);
    }
}
