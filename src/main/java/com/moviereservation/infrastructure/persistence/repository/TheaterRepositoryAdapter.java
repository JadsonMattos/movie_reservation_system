package com.moviereservation.infrastructure.persistence.repository;

import com.moviereservation.domain.model.showtime.Seat;
import com.moviereservation.domain.model.showtime.Theater;
import com.moviereservation.domain.port.TheaterRepositoryPort;
import com.moviereservation.infrastructure.persistence.mapper.SeatPersistenceMapper;
import com.moviereservation.infrastructure.persistence.mapper.TheaterPersistenceMapper;
import com.moviereservation.repository.SeatRepository;
import com.moviereservation.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TheaterRepositoryAdapter implements TheaterRepositoryPort {

    private final TheaterRepository theaterRepository;
    private final SeatRepository seatRepository;
    private final TheaterPersistenceMapper theaterMapper;
    private final SeatPersistenceMapper seatMapper;

    @Override
    public List<Theater> findAll() {
        return theaterRepository.findAll().stream()
                .map(theaterMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Theater> findById(Long id) {
        return theaterRepository.findById(id).map(theaterMapper::toDomain);
    }

    @Override
    public Theater save(Theater theater) {
        var entity = theaterMapper.toEntity(theater);
        entity = theaterRepository.save(entity);
        return theaterMapper.toDomain(entity);
    }

    @Override
    public void delete(Theater theater) {
        theaterRepository.delete(theaterMapper.toEntity(theater));
    }

    @Override
    public List<Seat> findSeatsByTheaterId(Long theaterId) {
        return seatRepository.findByTheaterId(theaterId).stream()
                .map(seatMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsSeat(Long theaterId, String rowLetter, Integer seatNumber) {
        return seatRepository.existsByTheaterIdAndRowLetterAndSeatNumber(theaterId, rowLetter, seatNumber);
    }

    @Override
    public Seat saveSeat(Seat seat) {
        var entity = seatMapper.toEntity(seat);
        entity = seatRepository.save(entity);
        return seatMapper.toDomain(entity);
    }

    @Override
    public Optional<Seat> findSeatById(Long seatId) {
        return seatRepository.findById(seatId).map(seatMapper::toDomain);
    }
}
