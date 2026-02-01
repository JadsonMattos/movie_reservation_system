package com.moviereservation.controller;

import com.moviereservation.application.dto.reservation.ReservationRequest;
import com.moviereservation.application.dto.reservation.ReservationResponse;
import com.moviereservation.application.dto.showtime.SeatAvailabilityResponse;
import com.moviereservation.application.port.in.ReservationUseCase;
import com.moviereservation.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservations", description = "Seat reservations (authenticated users)")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;

    @GetMapping("/showtimes/{showtimeId}/seats")
    @Operation(summary = "Get seat availability for a showtime")
    public SeatAvailabilityResponse getSeatAvailability(@PathVariable Long showtimeId) {
        return reservationUseCase.getSeatAvailability(showtimeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create reservation")
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request,
                                      @AuthenticationPrincipal UserPrincipal principal) {
        return reservationUseCase.create(request, principal.getId());
    }

    @GetMapping("/me")
    @Operation(summary = "Get my reservations")
    public List<ReservationResponse> getMyReservations(@AuthenticationPrincipal UserPrincipal principal) {
        return reservationUseCase.findMyReservations(principal.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel reservation")
    public void cancel(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        reservationUseCase.cancel(id, principal.getId());
    }
}
