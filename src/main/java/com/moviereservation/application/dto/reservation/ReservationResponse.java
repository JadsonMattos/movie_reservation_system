package com.moviereservation.application.dto.reservation;

import com.moviereservation.application.dto.theater.SeatResponse;
import com.moviereservation.domain.model.reservation.Reservation;
import com.moviereservation.domain.model.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ReservationResponse(
        Long id,
        Long showtimeId,
        String movieTitle,
        String theaterName,
        Instant showtimeStart,
        BigDecimal totalAmount,
        List<SeatResponse> seats,
        ReservationStatus status,
        Instant createdAt
) {

    public static ReservationResponse from(Reservation reservation) {
        BigDecimal total = reservation.getShowtime().getPricePerSeat()
                .multiply(BigDecimal.valueOf(reservation.getReservedSeats().size()));
        List<SeatResponse> seats = reservation.getReservedSeats().stream()
                .map(rs -> SeatResponse.from(rs.getSeat()))
                .toList();
        return new ReservationResponse(
                reservation.getId(),
                reservation.getShowtime().getId(),
                reservation.getShowtime().getMovie().getTitle(),
                reservation.getShowtime().getTheater().getName(),
                reservation.getShowtime().getStartTime(),
                total,
                seats,
                reservation.getStatus(),
                reservation.getCreatedAt());
    }
}
