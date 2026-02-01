package com.moviereservation.application.dto.theater;

import com.moviereservation.domain.model.showtime.Seat;

public record SeatResponse(Long id, String rowLetter, Integer seatNumber) {

    public static SeatResponse from(Seat seat) {
        return new SeatResponse(seat.getId(), seat.getRowLetter(), seat.getSeatNumber());
    }
}
