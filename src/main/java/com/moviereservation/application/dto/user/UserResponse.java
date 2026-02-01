package com.moviereservation.application.dto.user;

import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.model.user.UserRole;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        UserRole role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole());
    }
}
