package com.moviereservation.controller;

import com.moviereservation.application.dto.common.PageResult;
import com.moviereservation.application.dto.movie.GenreResponse;
import com.moviereservation.application.dto.movie.MovieResponse;
import com.moviereservation.application.dto.movie.MovieWithShowtimesResponse;
import com.moviereservation.application.dto.showtime.ShowtimeResponse;
import com.moviereservation.application.service.GenreService;
import com.moviereservation.application.service.MovieService;
import com.moviereservation.application.service.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies (Public)", description = "Public movie and showtime browsing")
public class PublicMovieController {

    private final MovieService movieService;
    private final GenreService genreService;
    private final ShowtimeService showtimeService;

    @GetMapping
    @Operation(summary = "List movies")
    public PageResult<MovieResponse> list(@PageableDefault(size = 20) org.springframework.data.domain.Pageable pageable,
                                          @RequestParam(required = false) Long genreId) {
        if (genreId != null) {
            return movieService.findByGenre(genreId, pageable.getPageNumber(), pageable.getPageSize());
        }
        return movieService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    @GetMapping("/genres")
    @Operation(summary = "List genres")
    public List<GenreResponse> listGenres() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by id")
    public MovieResponse getById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get movies with showtimes for a specific date")
    public List<MovieWithShowtimesResponse> getByDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<ShowtimeResponse> showtimes = showtimeService.findByDate(localDate);
        Map<Long, List<ShowtimeResponse>> byMovie = showtimes.stream()
                .collect(Collectors.groupingBy(ShowtimeResponse::movieId));

        return byMovie.entrySet().stream()
                .map(entry -> {
                    var movie = movieService.findById(entry.getKey());
                    return new MovieWithShowtimesResponse(
                            movie.id(),
                            movie.title(),
                            movie.description(),
                            movie.posterUrl(),
                            movie.genreName(),
                            movie.durationMinutes(),
                            entry.getValue());
                })
                .toList();
    }
}
