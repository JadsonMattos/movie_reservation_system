package com.moviereservation.application.dto.theater;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TheaterRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255)
        String name
) {}
