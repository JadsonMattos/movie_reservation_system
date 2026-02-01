package com.moviereservation.application.service;

import com.moviereservation.application.dto.movie.MovieRequest;
import com.moviereservation.application.dto.movie.MovieResponse;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.domain.model.movie.Genre;
import com.moviereservation.domain.model.movie.Movie;
import com.moviereservation.domain.port.GenreRepositoryPort;
import com.moviereservation.domain.port.MovieRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepositoryPort movieRepository;
    private final GenreRepositoryPort genreRepository;

    public com.moviereservation.application.dto.common.PageResult<MovieResponse> findAll(int page, int size) {
        var content = movieRepository.findAll(page, size).stream()
                .map(MovieResponse::from)
                .toList();
        long total = movieRepository.count();
        return com.moviereservation.application.dto.common.PageResult.of(content, total, page, size);
    }

    public com.moviereservation.application.dto.common.PageResult<MovieResponse> findByGenre(Long genreId, int page, int size) {
        var content = movieRepository.findByGenreId(genreId, page, size).stream()
                .map(MovieResponse::from)
                .toList();
        long total = movieRepository.countByGenreId(genreId);
        return com.moviereservation.application.dto.common.PageResult.of(content, total, page, size);
    }

    public MovieResponse findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", id));
        return MovieResponse.from(movie);
    }

    @Transactional
    public MovieResponse create(MovieRequest request) {
        Genre genre = genreRepository.findById(request.genreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre", request.genreId()));

        Movie movie = Movie.builder()
                .title(request.title().trim())
                .description(request.description() != null ? request.description().trim() : null)
                .posterUrl(request.posterUrl() != null ? request.posterUrl().trim() : null)
                .genre(genre)
                .durationMinutes(request.durationMinutes())
                .build();
        movie = movieRepository.save(movie);
        return MovieResponse.from(movie);
    }

    @Transactional
    public MovieResponse update(Long id, MovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", id));
        Genre genre = genreRepository.findById(request.genreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre", request.genreId()));

        movie.setTitle(request.title().trim());
        movie.setDescription(request.description() != null ? request.description().trim() : null);
        movie.setPosterUrl(request.posterUrl() != null ? request.posterUrl().trim() : null);
        movie.setGenre(genre);
        movie.setDurationMinutes(request.durationMinutes());
        movie = movieRepository.save(movie);
        return MovieResponse.from(movie);
    }

    @Transactional
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", id));
        movieRepository.delete(movie);
    }
}
