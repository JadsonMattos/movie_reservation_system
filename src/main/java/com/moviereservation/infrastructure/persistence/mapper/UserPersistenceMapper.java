package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.model.user.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(com.moviereservation.entity.User entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .fullName(entity.getFullName())
                .role(UserRole.valueOf(entity.getRole().name()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public com.moviereservation.entity.User toEntity(User domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.User.builder()
                .id(domain.getId())
                .email(domain.getEmail())
                .passwordHash(domain.getPasswordHash())
                .fullName(domain.getFullName())
                .role(domain.getRole() != null ? com.moviereservation.entity.UserRole.valueOf(domain.getRole().name()) : com.moviereservation.entity.UserRole.USER)
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
