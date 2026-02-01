package com.moviereservation.infrastructure.persistence.repository;

import com.moviereservation.domain.model.movie.Movie;
import com.moviereservation.domain.port.MovieRepositoryPort;
import com.moviereservation.infrastructure.persistence.mapper.MoviePersistenceMapper;
import com.moviereservation.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovieRepositoryAdapter implements MovieRepositoryPort {

    private final MovieRepository movieRepository;
    private final MoviePersistenceMapper mapper;

    @Override
    public List<Movie> findAll(int page, int size) {
        return movieRepository.findAll(PageRequest.of(page, size)).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Movie> findByGenreId(Long genreId, int page, int size) {
        return movieRepository.findByGenreId(genreId, PageRequest.of(page, size)).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Movie save(Movie movie) {
        var entity = mapper.toEntity(movie);
        entity = movieRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(Movie movie) {
        movieRepository.delete(mapper.toEntity(movie));
    }

    @Override
    public boolean existsByGenreId(Long genreId) {
        return movieRepository.existsByGenreId(genreId);
    }

    @Override
    public long count() {
        return movieRepository.count();
    }

    @Override
    public long countByGenreId(Long genreId) {
        return movieRepository.countByGenreId(genreId);
    }
}
