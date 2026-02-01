package com.moviereservation.application.port.in;

import com.moviereservation.application.dto.auth.AuthResponse;
import com.moviereservation.application.dto.auth.LoginRequest;
import com.moviereservation.application.dto.auth.SignupRequest;

public interface AuthUseCase {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
