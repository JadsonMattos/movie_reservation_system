package com.moviereservation.application.dto.movie;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,

        @Size(max = 2000)
        String description,

        @Size(max = 500)
        String posterUrl,

        @NotNull(message = "Genre is required")
        Long genreId,

        @NotNull(message = "Duration is required")
        @Min(value = 1, message = "Duration must be at least 1 minute")
        Integer durationMinutes
) {}
