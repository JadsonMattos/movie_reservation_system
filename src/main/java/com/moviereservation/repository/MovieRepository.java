package com.moviereservation.repository;

import com.moviereservation.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Page<Movie> findByGenreId(Long genreId, Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.genre ORDER BY m.title")
    List<Movie> findAllWithGenre(Pageable pageable);

    boolean existsByGenreId(Long genreId);

    long countByGenreId(Long genreId);
}
