package com.moviereservation.application.dto.showtime;

import com.moviereservation.domain.model.showtime.Showtime;

import java.math.BigDecimal;
import java.time.Instant;

public record ShowtimeResponse(
        Long id,
        Long movieId,
        String movieTitle,
        Long theaterId,
        String theaterName,
        Instant startTime,
        BigDecimal pricePerSeat
) {

    public static ShowtimeResponse from(Showtime showtime) {
        return new ShowtimeResponse(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getMovie().getTitle(),
                showtime.getTheater().getId(),
                showtime.getTheater().getName(),
                showtime.getStartTime(),
                showtime.getPricePerSeat());
    }
}
