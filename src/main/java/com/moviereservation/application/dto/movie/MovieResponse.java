package com.moviereservation.application.dto.movie;

import com.moviereservation.domain.model.movie.Movie;

public record MovieResponse(
        Long id,
        String title,
        String description,
        String posterUrl,
        Long genreId,
        String genreName,
        Integer durationMinutes
) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getPosterUrl(),
                movie.getGenre().getId(),
                movie.getGenre().getName(),
                movie.getDurationMinutes());
    }
}
