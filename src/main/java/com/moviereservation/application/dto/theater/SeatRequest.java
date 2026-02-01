package com.moviereservation.application.dto.theater;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SeatRequest(
        @NotBlank(message = "Row letter is required")
        @Size(max = 5)
        String rowLetter,

        @Min(value = 1, message = "Seat number must be at least 1")
        Integer seatNumber
) {}
