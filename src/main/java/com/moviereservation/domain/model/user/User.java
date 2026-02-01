package com.moviereservation.domain.model.user;

import lombok.*;

import java.time.Instant;

/**
 * Domain entity - User aggregate root.
 * No framework dependencies.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;
    private String email;
    private String passwordHash;
    private String fullName;
    private UserRole role;
    private Instant createdAt;
    private Instant updatedAt;

    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }
}
