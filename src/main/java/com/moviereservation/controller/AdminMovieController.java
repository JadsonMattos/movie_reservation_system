package com.moviereservation.controller;

import com.moviereservation.application.dto.common.PageResult;
import com.moviereservation.application.dto.movie.MovieRequest;
import com.moviereservation.application.dto.movie.MovieResponse;
import com.moviereservation.application.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Movies", description = "Movie management (admin only)")
public class AdminMovieController {

    private final MovieService movieService;

    @GetMapping
    @Operation(summary = "List all movies")
    public PageResult<MovieResponse> list(@PageableDefault(size = 20) org.springframework.data.domain.Pageable pageable,
                                          @RequestParam(required = false) Long genreId) {
        if (genreId != null) {
            return movieService.findByGenre(genreId, pageable.getPageNumber(), pageable.getPageSize());
        }
        return movieService.findAll(pageable.getPageNumber(), pageable.getPageSize());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie by id")
    public MovieResponse getById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create movie")
    public MovieResponse create(@Valid @RequestBody MovieRequest request) {
        return movieService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update movie")
    public MovieResponse update(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return movieService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete movie")
    public void delete(@PathVariable Long id) {
        movieService.delete(id);
    }
}
