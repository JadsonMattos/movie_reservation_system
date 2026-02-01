package com.moviereservation.infrastructure.persistence.repository;

import com.moviereservation.domain.model.movie.Genre;
import com.moviereservation.domain.port.GenreRepositoryPort;
import com.moviereservation.infrastructure.persistence.mapper.GenrePersistenceMapper;
import com.moviereservation.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreRepositoryAdapter implements GenreRepositoryPort {

    private final GenreRepository genreRepository;
    private final GenrePersistenceMapper mapper;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return genreRepository.existsByName(name);
    }

    @Override
    public Genre save(Genre genre) {
        var entity = mapper.toEntity(genre);
        entity = genreRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(Genre genre) {
        genreRepository.delete(mapper.toEntity(genre));
    }
}
