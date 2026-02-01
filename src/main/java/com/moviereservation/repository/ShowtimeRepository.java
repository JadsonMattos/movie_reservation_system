package com.moviereservation.repository;

import com.moviereservation.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    List<Showtime> findByMovieIdOrderByStartTimeAsc(Long movieId);

    List<Showtime> findByTheaterId(Long theaterId);

    @Query("""
            SELECT s FROM Showtime s
            LEFT JOIN FETCH s.movie m LEFT JOIN FETCH m.genre
            LEFT JOIN FETCH s.theater
            WHERE s.startTime >= :start AND s.startTime < :end
            ORDER BY s.startTime
            """)
    List<Showtime> findByDateRange(Instant start, Instant end);

}
