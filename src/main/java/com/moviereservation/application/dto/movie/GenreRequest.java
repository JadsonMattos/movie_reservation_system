package com.moviereservation.application.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100)
        String name
) {}
