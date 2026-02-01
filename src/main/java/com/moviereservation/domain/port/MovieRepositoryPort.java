package com.moviereservation.domain.port;

import com.moviereservation.domain.model.movie.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepositoryPort {

    List<Movie> findAll(int page, int size);

    List<Movie> findByGenreId(Long genreId, int page, int size);

    long count();

    long countByGenreId(Long genreId);

    Optional<Movie> findById(Long id);

    Movie save(Movie movie);

    void delete(Movie movie);

    boolean existsByGenreId(Long genreId);
}
