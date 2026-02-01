package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.movie.Genre;
import com.moviereservation.domain.model.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MoviePersistenceMapper {

    private final GenrePersistenceMapper genreMapper;

    public Movie toDomain(com.moviereservation.entity.Movie entity) {
        if (entity == null) return null;
        return Movie.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .posterUrl(entity.getPosterUrl())
                .genre(genreMapper.toDomain(entity.getGenre()))
                .durationMinutes(entity.getDurationMinutes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public com.moviereservation.entity.Movie toEntity(Movie domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.Movie.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .posterUrl(domain.getPosterUrl())
                .genre(genreMapper.toEntity(domain.getGenre()))
                .durationMinutes(domain.getDurationMinutes())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
