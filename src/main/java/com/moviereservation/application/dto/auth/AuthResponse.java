package com.moviereservation.application.dto.auth;

import com.moviereservation.domain.model.user.UserRole;

public record AuthResponse(
        String token,
        String type,
        Long id,
        String email,
        String fullName,
        UserRole role
) {
    public static AuthResponse of(String token, Long id, String email, String fullName, UserRole role) {
        return new AuthResponse(token, "Bearer", id, email, fullName, role);
    }
}
