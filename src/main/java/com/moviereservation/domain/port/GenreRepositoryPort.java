package com.moviereservation.domain.port;

import com.moviereservation.domain.model.movie.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryPort {

    List<Genre> findAll();

    Optional<Genre> findById(Long id);

    Optional<Genre> findByName(String name);

    boolean existsByName(String name);

    Genre save(Genre genre);

    void delete(Genre genre);
}
