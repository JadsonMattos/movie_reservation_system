package com.moviereservation.application.service;

import com.moviereservation.application.dto.movie.GenreRequest;
import com.moviereservation.application.dto.movie.GenreResponse;
import com.moviereservation.application.exception.ConflictException;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.domain.model.movie.Genre;
import com.moviereservation.domain.port.GenreRepositoryPort;
import com.moviereservation.domain.port.MovieRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepositoryPort genreRepository;
    private final MovieRepositoryPort movieRepository;

    public List<GenreResponse> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreResponse::from)
                .toList();
    }

    public GenreResponse findById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        return GenreResponse.from(genre);
    }

    @Transactional
    public GenreResponse create(GenreRequest request) {
        if (genreRepository.existsByName(request.name())) {
            throw new ConflictException("Genre already exists: " + request.name());
        }
        Genre genre = Genre.builder()
                .name(request.name().trim())
                .build();
        genre = genreRepository.save(genre);
        return GenreResponse.from(genre);
    }

    @Transactional
    public GenreResponse update(Long id, GenreRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        if (!genre.getName().equalsIgnoreCase(request.name()) && genreRepository.existsByName(request.name())) {
            throw new ConflictException("Genre already exists: " + request.name());
        }
        genre.setName(request.name().trim());
        genre = genreRepository.save(genre);
        return GenreResponse.from(genre);
    }

    @Transactional
    public void delete(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        if (movieRepository.existsByGenreId(id)) {
            throw new ConflictException("Cannot delete genre: movies are using it");
        }
        genreRepository.delete(genre);
    }
}
