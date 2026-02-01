package com.moviereservation.application.service;

import com.moviereservation.application.dto.auth.AuthResponse;
import com.moviereservation.application.dto.auth.LoginRequest;
import com.moviereservation.application.dto.auth.SignupRequest;
import com.moviereservation.application.exception.EmailAlreadyExistsException;
import com.moviereservation.application.exception.InvalidCredentialsException;
import com.moviereservation.application.port.in.AuthUseCase;
import com.moviereservation.application.port.out.PasswordEncoderPort;
import com.moviereservation.application.port.out.TokenGeneratorPort;
import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.model.user.UserRole;
import com.moviereservation.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final TokenGeneratorPort tokenGenerator;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .email(request.email().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName().trim())
                .role(UserRole.USER)
                .build();
        user = userRepository.save(user);

        String token = tokenGenerator.generate(user.getId(), user.getEmail(), user.getRole().name());
        return AuthResponse.of(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenGenerator.generate(user.getId(), user.getEmail(), user.getRole().name());
        return AuthResponse.of(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
    }

}
