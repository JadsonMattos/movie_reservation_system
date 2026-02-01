package com.moviereservation.controller;

import com.moviereservation.application.dto.showtime.ShowtimeRequest;
import com.moviereservation.application.dto.showtime.ShowtimeResponse;
import com.moviereservation.application.service.ShowtimeService;
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
@RequestMapping("/api/admin/showtimes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Showtimes", description = "Showtime management (admin only)")
public class AdminShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping
    @Operation(summary = "List showtimes for a date")
    public List<ShowtimeResponse> listByDate(@RequestParam String date) {
        return showtimeService.findByDate(java.time.LocalDate.parse(date));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get showtime by id")
    public ShowtimeResponse getById(@PathVariable Long id) {
        return showtimeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create showtime")
    public ShowtimeResponse create(@Valid @RequestBody ShowtimeRequest request) {
        return showtimeService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update showtime")
    public ShowtimeResponse update(@PathVariable Long id, @Valid @RequestBody ShowtimeRequest request) {
        return showtimeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete showtime")
    public void delete(@PathVariable Long id) {
        showtimeService.delete(id);
    }
}
