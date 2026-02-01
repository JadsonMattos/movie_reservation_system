package com.moviereservation.domain.port;

import com.moviereservation.domain.model.showtime.Seat;
import com.moviereservation.domain.model.showtime.Theater;

import java.util.List;
import java.util.Optional;

public interface TheaterRepositoryPort {

    List<Theater> findAll();

    Optional<Theater> findById(Long id);

    Theater save(Theater theater);

    void delete(Theater theater);

    List<Seat> findSeatsByTheaterId(Long theaterId);

    boolean existsSeat(Long theaterId, String rowLetter, Integer seatNumber);

    Seat saveSeat(Seat seat);

    Optional<Seat> findSeatById(Long seatId);
}
