package com.moviereservation.application.service;

import com.moviereservation.application.dto.showtime.ShowtimeRequest;
import com.moviereservation.application.dto.showtime.ShowtimeResponse;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.domain.model.movie.Movie;
import com.moviereservation.domain.model.showtime.Showtime;
import com.moviereservation.domain.model.showtime.Theater;
import com.moviereservation.domain.port.MovieRepositoryPort;
import com.moviereservation.domain.port.ShowtimeRepositoryPort;
import com.moviereservation.domain.port.TheaterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

    private final ShowtimeRepositoryPort showtimeRepository;
    private final MovieRepositoryPort movieRepository;
    private final TheaterRepositoryPort theaterRepository;

    public List<ShowtimeResponse> findByDate(LocalDate date) {
        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        return showtimeRepository.findByDateRange(start, end).stream()
                .map(ShowtimeResponse::from)
                .toList();
    }

    public ShowtimeResponse findById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", id));
        return ShowtimeResponse.from(showtime);
    }

    public List<ShowtimeResponse> findByMovieId(Long movieId) {
        return showtimeRepository.findByMovieIdOrderByStartTimeAsc(movieId).stream()
                .map(ShowtimeResponse::from)
                .toList();
    }

    @Transactional
    public ShowtimeResponse create(ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", request.movieId()));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater", request.theaterId()));

        if (request.startTime().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Showtime must be in the future");
        }

        long movieEndEpoch = request.startTime().getEpochSecond() + movie.getDurationMinutes() * 60L;
        Instant movieEnd = Instant.ofEpochSecond(movieEndEpoch);
        List<Showtime> existing = showtimeRepository.findByTheaterId(theater.getId());
        for (Showtime s : existing) {
            long sEnd = s.getStartTime().getEpochSecond() + s.getMovie().getDurationMinutes() * 60L;
            if (request.startTime().getEpochSecond() < sEnd && movieEnd.getEpochSecond() > s.getStartTime().getEpochSecond()) {
                throw new IllegalArgumentException("Showtime overlaps with existing showtime in same theater");
            }
        }

        Showtime showtime = Showtime.builder()
                .movie(movie)
                .theater(theater)
                .startTime(request.startTime())
                .pricePerSeat(request.pricePerSeat())
                .build();
        showtime = showtimeRepository.save(showtime);
        return ShowtimeResponse.from(showtime);
    }

    @Transactional
    public ShowtimeResponse update(Long id, ShowtimeRequest request) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", id));
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie", request.movieId()));
        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater", request.theaterId()));

        if (request.startTime().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Showtime must be in the future");
        }

        long movieEndEpoch = request.startTime().getEpochSecond() + movie.getDurationMinutes() * 60L;
        Instant movieEnd = Instant.ofEpochSecond(movieEndEpoch);
        List<Showtime> existing = showtimeRepository.findByTheaterId(theater.getId()).stream()
                .filter(s -> !s.getId().equals(id))
                .toList();
        for (Showtime s : existing) {
            long sEnd = s.getStartTime().getEpochSecond() + s.getMovie().getDurationMinutes() * 60L;
            if (request.startTime().getEpochSecond() < sEnd && movieEnd.getEpochSecond() > s.getStartTime().getEpochSecond()) {
                throw new IllegalArgumentException("Showtime overlaps with existing showtime in same theater");
            }
        }

        showtime.setMovie(movie);
        showtime.setTheater(theater);
        showtime.setStartTime(request.startTime());
        showtime.setPricePerSeat(request.pricePerSeat());
        showtime = showtimeRepository.save(showtime);
        return ShowtimeResponse.from(showtime);
    }

    @Transactional
    public void delete(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", id));
        showtimeRepository.delete(showtime);
    }
}
