package com.moviereservation.controller;

import com.moviereservation.application.dto.movie.GenreRequest;
import com.moviereservation.application.dto.movie.GenreResponse;
import com.moviereservation.application.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/genres")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Genres", description = "Genre management (admin only)")
public class AdminGenreController {

    private final GenreService genreService;

    @GetMapping
    @Operation(summary = "List all genres")
    public List<GenreResponse> list() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get genre by id")
    public GenreResponse getById(@PathVariable Long id) {
        return genreService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create genre")
    public GenreResponse create(@Valid @RequestBody GenreRequest request) {
        return genreService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update genre")
    public GenreResponse update(@PathVariable Long id, @Valid @RequestBody GenreRequest request) {
        return genreService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete genre")
    public void delete(@PathVariable Long id) {
        genreService.delete(id);
    }
}
