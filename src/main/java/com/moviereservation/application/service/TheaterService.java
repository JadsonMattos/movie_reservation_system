package com.moviereservation.application.service;

import com.moviereservation.application.dto.theater.SeatRequest;
import com.moviereservation.application.dto.theater.SeatResponse;
import com.moviereservation.application.dto.theater.TheaterRequest;
import com.moviereservation.application.dto.theater.TheaterResponse;
import com.moviereservation.application.exception.ConflictException;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.domain.model.showtime.Seat;
import com.moviereservation.domain.model.showtime.Theater;
import com.moviereservation.domain.port.TheaterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepositoryPort theaterRepository;

    public List<TheaterResponse> findAll() {
        return theaterRepository.findAll().stream()
                .map(TheaterResponse::fromBasic)
                .toList();
    }

    public TheaterResponse findById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", id));
        List<Seat> seats = theaterRepository.findSeatsByTheaterId(id);
        List<SeatResponse> seatResponses = seats.stream().map(SeatResponse::from).toList();
        return TheaterResponse.from(theater, seatResponses);
    }

    @Transactional
    public TheaterResponse create(TheaterRequest request) {
        Theater theater = Theater.builder()
                .name(request.name().trim())
                .build();
        theater = theaterRepository.save(theater);
        return TheaterResponse.fromBasic(theater);
    }

    @Transactional
    public TheaterResponse update(Long id, TheaterRequest request) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", id));
        theater.setName(request.name().trim());
        theater = theaterRepository.save(theater);
        return TheaterResponse.fromBasic(theater);
    }

    @Transactional
    public void delete(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", id));
        theaterRepository.delete(theater);
    }

    @Transactional
    public SeatResponse addSeat(Long theaterId, SeatRequest request) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater", theaterId));

        if (theaterRepository.existsSeat(theaterId, request.rowLetter(), request.seatNumber())) {
            throw new ConflictException("Seat already exists: " + request.rowLetter() + request.seatNumber());
        }

        Seat seat = Seat.builder()
                .theater(theater)
                .rowLetter(request.rowLetter().trim())
                .seatNumber(request.seatNumber())
                .build();
        seat = theaterRepository.saveSeat(seat);
        return SeatResponse.from(seat);
    }
}
