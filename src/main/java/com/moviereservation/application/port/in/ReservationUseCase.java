package com.moviereservation.application.port.in;

import com.moviereservation.application.dto.reservation.ReservationRequest;
import com.moviereservation.application.dto.reservation.ReservationResponse;
import com.moviereservation.application.dto.showtime.SeatAvailabilityResponse;

import java.util.List;

public interface ReservationUseCase {

    SeatAvailabilityResponse getSeatAvailability(Long showtimeId);

    ReservationResponse create(ReservationRequest request, Long userId);

    List<ReservationResponse> findMyReservations(Long userId);

    void cancel(Long reservationId, Long userId);
}
