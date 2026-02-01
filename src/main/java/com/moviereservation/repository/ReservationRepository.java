package com.moviereservation.repository;

import com.moviereservation.entity.Reservation;
import com.moviereservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Reservation> findByShowtimeId(Long showtimeId);
}
