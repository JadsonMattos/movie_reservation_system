package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.movie.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenrePersistenceMapper {

    public Genre toDomain(com.moviereservation.entity.Genre entity) {
        if (entity == null) return null;
        return Genre.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public com.moviereservation.entity.Genre toEntity(Genre domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.Genre.builder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }
}
