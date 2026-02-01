package com.moviereservation.application.service;

import com.moviereservation.application.dto.reservation.ReservationRequest;
import com.moviereservation.application.dto.reservation.ReservationResponse;
import com.moviereservation.application.dto.showtime.SeatAvailabilityResponse;
import com.moviereservation.application.dto.theater.SeatResponse;
import com.moviereservation.application.exception.ConflictException;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.application.port.in.ReservationUseCase;
import com.moviereservation.domain.model.reservation.Reservation;
import com.moviereservation.domain.model.reservation.ReservationStatus;
import com.moviereservation.domain.model.reservation.ReservedSeat;
import com.moviereservation.domain.model.showtime.Seat;
import com.moviereservation.domain.model.showtime.Showtime;
import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.port.ReservationRepositoryPort;
import com.moviereservation.domain.port.SeatLockPort;
import com.moviereservation.domain.port.ShowtimeRepositoryPort;
import com.moviereservation.domain.port.TheaterRepositoryPort;
import com.moviereservation.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationUseCase {

    private final ReservationRepositoryPort reservationRepository;
    private final SeatLockPort seatLockPort;
    private final ShowtimeRepositoryPort showtimeRepository;
    private final TheaterRepositoryPort theaterRepository;
    private final UserRepositoryPort userRepository;

    @Override
    public SeatAvailabilityResponse getSeatAvailability(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", showtimeId));

        List<Seat> allSeats = theaterRepository.findSeatsByTheaterId(showtime.getTheater().getId());
        Set<Long> reservedIds = reservationRepository.findReservedSeatIdsByShowtime(showtimeId);

        List<SeatResponse> available = allSeats.stream()
                .filter(s -> !reservedIds.contains(s.getId()))
                .map(SeatResponse::from)
                .toList();
        List<SeatResponse> reserved = allSeats.stream()
                .filter(s -> reservedIds.contains(s.getId()))
                .map(SeatResponse::from)
                .toList();

        return new SeatAvailabilityResponse(
                showtimeId,
                allSeats.stream().map(SeatResponse::from).toList(),
                available,
                reserved);
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Showtime showtime = showtimeRepository.findById(request.showtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", request.showtimeId()));

        if (showtime.getStartTime().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Cannot reserve seats for past showtime");
        }

        Long theaterId = showtime.getTheater().getId();
        for (Long seatId : request.seatIds()) {
            Seat seat = theaterRepository.findSeatById(seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId));
            if (!seat.getTheater().getId().equals(theaterId)) {
                throw new IllegalArgumentException("All seats must belong to showtime theater");
            }
        }

        Set<Long> reservedIds = reservationRepository.findReservedSeatIdsByShowtime(request.showtimeId());
        Set<Long> requestedIds = new HashSet<>(request.seatIds());
        requestedIds.retainAll(reservedIds);
        if (!requestedIds.isEmpty()) {
            throw new ConflictException("One or more seats are already reserved");
        }

        if (!seatLockPort.tryLock(request.showtimeId(), request.seatIds(), Duration.ofSeconds(30))) {
            throw new ConflictException("One or more seats are being reserved by another user");
        }
        try {
            return doCreateReservation(user, showtime, request);
        } finally {
            seatLockPort.unlockAll(request.showtimeId(), request.seatIds());
        }
    }

    @Transactional
    protected ReservationResponse doCreateReservation(User user, Showtime showtime, ReservationRequest request) {
        Set<Long> reservedIds = reservationRepository.findReservedSeatIdsByShowtime(request.showtimeId());
        Set<Long> requestedIds = new HashSet<>(request.seatIds());
        requestedIds.retainAll(reservedIds);
        if (!requestedIds.isEmpty()) {
            throw new ConflictException("One or more seats are already reserved");
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .showtime(showtime)
                .status(ReservationStatus.CONFIRMED)
                .build();
        reservation = reservationRepository.save(reservation);

        for (Long seatId : request.seatIds()) {
            Seat seat = theaterRepository.findSeatById(seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId));
            ReservedSeat reservedSeat = ReservedSeat.builder()
                    .reservation(reservation)
                    .seat(seat)
                    .showtime(showtime)
                    .build();
            reservationRepository.saveReservedSeat(reservedSeat);
            reservation.getReservedSeats().add(reservedSeat);
        }

        return ReservationResponse.from(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> findMyReservations(Long userId) {
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void cancel(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (!reservation.belongsTo(user)) {
            throw new IllegalArgumentException("Cannot cancel another user's reservation");
        }
        reservation.cancel();
        reservationRepository.save(reservation);
    }
}
