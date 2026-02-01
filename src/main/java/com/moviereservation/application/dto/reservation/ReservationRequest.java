package com.moviereservation.application.dto.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationRequest(
        @NotNull(message = "Showtime is required")
        Long showtimeId,

        @NotEmpty(message = "At least one seat is required")
        List<Long> seatIds
) {}
