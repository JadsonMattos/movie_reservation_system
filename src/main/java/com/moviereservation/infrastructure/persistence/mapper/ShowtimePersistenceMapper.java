package com.moviereservation.infrastructure.persistence.mapper;

import com.moviereservation.domain.model.showtime.Showtime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShowtimePersistenceMapper {

    private final MoviePersistenceMapper movieMapper;
    private final TheaterPersistenceMapper theaterMapper;

    public Showtime toDomain(com.moviereservation.entity.Showtime entity) {
        if (entity == null) return null;
        return Showtime.builder()
                .id(entity.getId())
                .movie(movieMapper.toDomain(entity.getMovie()))
                .theater(theaterMapper.toDomain(entity.getTheater()))
                .startTime(entity.getStartTime())
                .pricePerSeat(entity.getPricePerSeat())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public com.moviereservation.entity.Showtime toEntity(Showtime domain) {
        if (domain == null) return null;
        return com.moviereservation.entity.Showtime.builder()
                .id(domain.getId())
                .movie(movieMapper.toEntity(domain.getMovie()))
                .theater(theaterMapper.toEntity(domain.getTheater()))
                .startTime(domain.getStartTime())
                .pricePerSeat(domain.getPricePerSeat())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
