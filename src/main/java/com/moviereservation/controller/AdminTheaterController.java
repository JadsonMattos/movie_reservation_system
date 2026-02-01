package com.moviereservation.controller;

import com.moviereservation.application.dto.theater.SeatRequest;
import com.moviereservation.application.dto.theater.SeatResponse;
import com.moviereservation.application.dto.theater.TheaterRequest;
import com.moviereservation.application.dto.theater.TheaterResponse;
import com.moviereservation.application.service.TheaterService;
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
@RequestMapping("/api/admin/theaters")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Theaters", description = "Theater and seat management (admin only)")
public class AdminTheaterController {

    private final TheaterService theaterService;

    @GetMapping
    @Operation(summary = "List all theaters")
    public List<TheaterResponse> list() {
        return theaterService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get theater with seats")
    public TheaterResponse getById(@PathVariable Long id) {
        return theaterService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create theater")
    public TheaterResponse create(@Valid @RequestBody TheaterRequest request) {
        return theaterService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update theater")
    public TheaterResponse update(@PathVariable Long id, @Valid @RequestBody TheaterRequest request) {
        return theaterService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete theater")
    public void delete(@PathVariable Long id) {
        theaterService.delete(id);
    }

    @PostMapping("/{theaterId}/seats")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add seat to theater")
    public SeatResponse addSeat(@PathVariable Long theaterId, @Valid @RequestBody SeatRequest request) {
        return theaterService.addSeat(theaterId, request);
    }
}
