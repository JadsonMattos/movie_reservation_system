package com.moviereservation.application.dto.movie;

import com.moviereservation.domain.model.movie.Genre;

public record GenreResponse(Long id, String name) {

    public static GenreResponse from(Genre genre) {
        return new GenreResponse(genre.getId(), genre.getName());
    }
}
