package com.moviereservation.application.dto.showtime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record ShowtimeRequest(
        @NotNull(message = "Movie is required")
        Long movieId,

        @NotNull(message = "Theater is required")
        Long theaterId,

        @NotNull(message = "Start time is required")
        Instant startTime,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0", message = "Price must be non-negative")
        BigDecimal pricePerSeat
) {}
