package com.moviereservation.application.dto.theater;

import com.moviereservation.domain.model.showtime.Theater;

import java.util.List;

public record TheaterResponse(
        Long id,
        String name,
        List<SeatResponse> seats
) {

    public static TheaterResponse from(Theater theater, List<SeatResponse> seats) {
        return new TheaterResponse(theater.getId(), theater.getName(), seats);
    }

    public static TheaterResponse fromBasic(Theater theater) {
        return new TheaterResponse(theater.getId(), theater.getName(), List.of());
    }
}
