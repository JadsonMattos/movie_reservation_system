package com.moviereservation.infrastructure.persistence.repository;

import com.moviereservation.domain.model.showtime.Showtime;
import com.moviereservation.domain.port.ShowtimeRepositoryPort;
import com.moviereservation.infrastructure.persistence.mapper.ShowtimePersistenceMapper;
import com.moviereservation.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShowtimeRepositoryAdapter implements ShowtimeRepositoryPort {

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimePersistenceMapper mapper;

    @Override
    public List<Showtime> findByDateRange(Instant start, Instant end) {
        return showtimeRepository.findByDateRange(start, end).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Showtime> findByMovieIdOrderByStartTimeAsc(Long movieId) {
        return showtimeRepository.findByMovieIdOrderByStartTimeAsc(movieId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Showtime> findByTheaterId(Long theaterId) {
        return showtimeRepository.findByTheaterId(theaterId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Showtime> findById(Long id) {
        return showtimeRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Showtime save(Showtime showtime) {
        var entity = mapper.toEntity(showtime);
        entity = showtimeRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(Showtime showtime) {
        showtimeRepository.delete(mapper.toEntity(showtime));
    }
}
