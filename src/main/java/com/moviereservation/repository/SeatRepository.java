package com.moviereservation.repository;

import com.moviereservation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByTheaterId(Long theaterId);

    boolean existsByTheaterIdAndRowLetterAndSeatNumber(Long theaterId, String rowLetter, Integer seatNumber);
}
