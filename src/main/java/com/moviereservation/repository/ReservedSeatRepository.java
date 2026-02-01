package com.moviereservation.repository;

import com.moviereservation.entity.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {

    @Query("SELECT rs.seat.id FROM ReservedSeat rs WHERE rs.showtime.id = :showtimeId AND rs.reservation.status = com.moviereservation.entity.ReservationStatus.CONFIRMED")
    Set<Long> findReservedSeatIdsByShowtime(Long showtimeId);
}
