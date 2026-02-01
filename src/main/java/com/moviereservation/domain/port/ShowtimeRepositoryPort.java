package com.moviereservation.domain.port;

import com.moviereservation.domain.model.showtime.Showtime;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShowtimeRepositoryPort {

    List<Showtime> findByDateRange(Instant start, Instant end);

    List<Showtime> findByMovieIdOrderByStartTimeAsc(Long movieId);

    List<Showtime> findByTheaterId(Long theaterId);

    Optional<Showtime> findById(Long id);

    Showtime save(Showtime showtime);

    void delete(Showtime showtime);
}
