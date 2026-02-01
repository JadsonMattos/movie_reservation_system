package com.moviereservation.domain.model.movie;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    private Long id;
    private String title;
    private String description;
    private String posterUrl;
    private Genre genre;
    private Integer durationMinutes;
    private Instant createdAt;
    private Instant updatedAt;
}
