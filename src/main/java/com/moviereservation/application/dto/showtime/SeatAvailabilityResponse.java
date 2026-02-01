package com.moviereservation.application.dto.showtime;

import com.moviereservation.application.dto.theater.SeatResponse;

import java.util.List;

public record SeatAvailabilityResponse(
        Long showtimeId,
        List<SeatResponse> seats,
        List<SeatResponse> availableSeats,
        List<SeatResponse> reservedSeats
) {}
