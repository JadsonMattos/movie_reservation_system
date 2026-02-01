package com.moviereservation.domain.port;

import com.moviereservation.domain.model.reservation.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepositoryPort {

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Set<Long> findReservedSeatIdsByShowtime(Long showtimeId);

    Reservation save(Reservation reservation);

    void saveReservedSeat(com.moviereservation.domain.model.reservation.ReservedSeat reservedSeat);
}
