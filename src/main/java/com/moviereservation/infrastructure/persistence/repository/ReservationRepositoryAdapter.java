package com.moviereservation.infrastructure.persistence.repository;

import com.moviereservation.domain.model.reservation.Reservation;
import com.moviereservation.domain.model.reservation.ReservedSeat;
import com.moviereservation.domain.port.ReservationRepositoryPort;
import com.moviereservation.infrastructure.persistence.mapper.ReservationPersistenceMapper;
import com.moviereservation.infrastructure.persistence.mapper.SeatPersistenceMapper;
import com.moviereservation.infrastructure.persistence.mapper.ShowtimePersistenceMapper;
import com.moviereservation.repository.ReservationRepository;
import com.moviereservation.repository.ReservedSeatRepository;
import com.moviereservation.repository.SeatRepository;
import com.moviereservation.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepositoryPort {

    private final ReservationRepository reservationRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ReservationPersistenceMapper mapper;
    private final SeatPersistenceMapper seatMapper;
    private final ShowtimePersistenceMapper showtimeMapper;

    @Override
    public List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Set<Long> findReservedSeatIdsByShowtime(Long showtimeId) {
        return reservedSeatRepository.findReservedSeatIdsByShowtime(showtimeId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        var entity = mapper.toEntity(reservation);
        entity = reservationRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public void saveReservedSeat(ReservedSeat reservedSeat) {
        var reservationEntity = reservedSeat.getReservation() != null
                ? reservationRepository.findById(reservedSeat.getReservation().getId()).orElseThrow()
                : null;
        var seatEntity = reservedSeat.getSeat() != null
                ? seatRepository.findById(reservedSeat.getSeat().getId()).orElseThrow()
                : null;
        var showtimeEntity = reservedSeat.getShowtime() != null
                ? showtimeRepository.findById(reservedSeat.getShowtime().getId()).orElseThrow()
                : null;
        var entity = com.moviereservation.entity.ReservedSeat.builder()
                .reservation(reservationEntity)
                .seat(seatEntity)
                .showtime(showtimeEntity)
                .build();
        reservedSeatRepository.save(entity);
    }
}
