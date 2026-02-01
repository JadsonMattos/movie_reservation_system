package com.moviereservation.service;

import com.moviereservation.application.dto.auth.LoginRequest;
import com.moviereservation.application.dto.auth.SignupRequest;
import com.moviereservation.application.exception.EmailAlreadyExistsException;
import com.moviereservation.application.exception.InvalidCredentialsException;
import com.moviereservation.application.port.out.PasswordEncoderPort;
import com.moviereservation.application.port.out.TokenGeneratorPort;
import com.moviereservation.application.service.AuthService;
import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.model.user.UserRole;
import com.moviereservation.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepository;
    @Mock
    private PasswordEncoderPort passwordEncoder;
    @Mock
    private TokenGeneratorPort tokenGenerator;

    @InjectMocks
    private AuthService authService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = User.builder()
                .id(1L)
                .email("user@test.com")
                .passwordHash("hashed")
                .fullName("Test User")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void signup_shouldCreateUserAndReturnToken() {
        SignupRequest request = new SignupRequest("new@test.com", "password123", "New User");
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return User.builder()
                    .id(2L)
                    .email(u.getEmail())
                    .passwordHash(u.getPasswordHash())
                    .fullName(u.getFullName())
                    .role(u.getRole())
                    .build();
        });
        when(tokenGenerator.generate(any(), anyString(), anyString())).thenReturn("token");

        var response = authService.signup(request);

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.email()).isEqualTo("new@test.com");
        assertThat(response.role()).isEqualTo(UserRole.USER);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_shouldThrowWhenEmailExists() {
        SignupRequest request = new SignupRequest("user@test.com", "password123", "Test");
        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("user@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnTokenWhenCredentialsValid() {
        LoginRequest request = new LoginRequest("user@test.com", "password123");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        when(tokenGenerator.generate(any(), anyString(), anyString())).thenReturn("token");

        var response = authService.login(request);

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.email()).isEqualTo("user@test.com");
    }

    @Test
    void login_shouldThrowWhenUserNotFound() {
        LoginRequest request = new LoginRequest("unknown@test.com", "password123");
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_shouldThrowWhenPasswordInvalid() {
        LoginRequest request = new LoginRequest("user@test.com", "wrong");
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
