package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.showtime.Theater;
import org.springframework.stereotype.Component;

@Component
public class TheaterPersistenceMapper {

    public Theater toDomain(com.moviereservation.entity.Theater entity) {
        if (entity == null) return null;
        return Theater.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public com.moviereservation.entity.Theater toEntity(Theater domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.Theater.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}
