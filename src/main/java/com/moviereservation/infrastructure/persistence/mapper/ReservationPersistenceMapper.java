package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.reservation.Reservation;
import com.moviereservation.domain.model.reservation.ReservationStatus;
import com.moviereservation.domain.model.reservation.ReservedSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationPersistenceMapper {

    private final UserPersistenceMapper userMapper;
    private final ShowtimePersistenceMapper showtimeMapper;
    private final SeatPersistenceMapper seatMapper;

    public Reservation toDomain(com.moviereservation.entity.Reservation entity) {
        if (entity == null) return null;
        var reservedSeats = entity.getReservedSeats() != null
                ? entity.getReservedSeats().stream()
                .map(rs -> ReservedSeat.builder()
                        .id(rs.getId())
                        .seat(seatMapper.toDomain(rs.getSeat()))
                        .showtime(showtimeMapper.toDomain(rs.getShowtime()))
                        .build())
                .toList()
                : new ArrayList<ReservedSeat>();
        return Reservation.builder()
                .id(entity.getId())
                .user(userMapper.toDomain(entity.getUser()))
                .showtime(showtimeMapper.toDomain(entity.getShowtime()))
                .status(ReservationStatus.valueOf(entity.getStatus().name()))
                .createdAt(entity.getCreatedAt())
                .reservedSeats(reservedSeats)
                .build();
    }

    public com.moviereservation.entity.Reservation toEntity(Reservation domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.Reservation.builder()
                .id(domain.getId())
                .user(userMapper.toEntity(domain.getUser()))
                .showtime(showtimeMapper.toEntity(domain.getShowtime()))
                .status(domain.getStatus() != null ? com.moviereservation.entity.ReservationStatus.valueOf(domain.getStatus().name()) : com.moviereservation.entity.ReservationStatus.CONFIRMED)
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
