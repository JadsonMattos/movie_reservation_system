package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.showtime.Seat;
import com.moviereservation.domain.model.showtime.Theater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatPersistenceMapper {

    private final TheaterPersistenceMapper theaterMapper;

    public Seat toDomain(com.moviereservation.entity.Seat entity) {
        if (entity == null) return null;
        Theater theater = entity.getTheater() != null ? theaterMapper.toDomain(entity.getTheater()) : null;
        return Seat.builder()
                .id(entity.getId())
                .theater(theater)
                .rowLetter(entity.getRowLetter())
                .seatNumber(entity.getSeatNumber())
                .build();
    }

    public com.moviereservation.entity.Seat toEntity(Seat domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.Seat.builder()
                .id(domain.getId())
                .theater(theaterMapper.toEntity(domain.getTheater()))
                .rowLetter(domain.getRowLetter())
                .seatNumber(domain.getSeatNumber())
                .build();
    }
}
