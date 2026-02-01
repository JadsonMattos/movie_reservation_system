package com.moviereservation.domain.model.showtime;

import com.moviereservation.domain.model.movie.Movie;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime {

    private Long id;
    private Movie movie;
    private Theater theater;
    private Instant startTime;
    private BigDecimal pricePerSeat;
    private Instant createdAt;

    public boolean isInThePast() {
        return startTime.isBefore(Instant.now());
    }

    public Instant getEndTime() {
        return startTime.plusSeconds(movie.getDurationMinutes() * 60L);
    }
}
