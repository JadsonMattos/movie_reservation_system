package com.moviereservation.application.service;

import com.moviereservation.application.dto.admin.CapacityReportResponse;
import com.moviereservation.application.dto.admin.RevenueReportResponse;
import com.moviereservation.application.dto.reservation.ReservationResponse;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.domain.model.reservation.Reservation;
import com.moviereservation.domain.model.reservation.ReservationStatus;
import com.moviereservation.domain.model.showtime.Showtime;
import com.moviereservation.domain.port.ReservationRepositoryPort;
import com.moviereservation.domain.port.ShowtimeRepositoryPort;
import com.moviereservation.domain.port.TheaterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final ReservationRepositoryPort reservationRepository;
    private final ShowtimeRepositoryPort showtimeRepository;
    private final TheaterRepositoryPort theaterRepository;

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAllReservations(Long movieId, Long userId, LocalDate fromDate, LocalDate toDate) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED || r.getStatus() == ReservationStatus.CANCELLED)
                .filter(r -> movieId == null || r.getShowtime().getMovie().getId().equals(movieId))
                .filter(r -> userId == null || r.getUser().getId().equals(userId))
                .filter(r -> fromDate == null || !r.getShowtime().getStartTime().atOffset(ZoneOffset.UTC).toLocalDate().isBefore(fromDate))
                .filter(r -> toDate == null || !r.getShowtime().getStartTime().atOffset(ZoneOffset.UTC).toLocalDate().isAfter(toDate))
                .map(ReservationResponse::from)
                .toList();
    }

    public CapacityReportResponse getShowtimeCapacity(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime", showtimeId));

        int totalSeats = theaterRepository.findSeatsByTheaterId(showtime.getTheater().getId()).size();
        int reservedSeats = reservationRepository.findReservedSeatIdsByShowtime(showtimeId).size();
        int availableSeats = totalSeats - reservedSeats;

        BigDecimal occupancyPercent = totalSeats > 0
                ? BigDecimal.valueOf(reservedSeats).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(totalSeats), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new CapacityReportResponse(
                showtimeId,
                showtime.getMovie().getTitle(),
                showtime.getTheater().getName(),
                totalSeats,
                reservedSeats,
                availableSeats,
                occupancyPercent);
    }

    @Transactional(readOnly = true)
    public RevenueReportResponse getRevenue(LocalDate fromDate, LocalDate toDate) {
        Instant from = fromDate != null ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant() : Instant.EPOCH;
        Instant to = toDate != null ? toDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant() : Instant.now().plusSeconds(86400L * 365);

        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .filter(r -> !r.getShowtime().getStartTime().isBefore(from))
                .filter(r -> r.getShowtime().getStartTime().isBefore(to))
                .toList();

        BigDecimal totalRevenue = reservations.stream()
                .map(r -> r.getShowtime().getPricePerSeat().multiply(BigDecimal.valueOf(r.getReservedSeats().size())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, List<Reservation>> byMovie = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getShowtime().getMovie().getId()));
        Map<Long, List<Reservation>> byTheater = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getShowtime().getTheater().getId()));

        List<RevenueReportResponse.RevenueByMovie> byMovieList = byMovie.entrySet().stream()
                .map(e -> {
                    BigDecimal rev = e.getValue().stream()
                            .map(r -> r.getShowtime().getPricePerSeat().multiply(BigDecimal.valueOf(r.getReservedSeats().size())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String title = e.getValue().getFirst().getShowtime().getMovie().getTitle();
                    return new RevenueReportResponse.RevenueByMovie(e.getKey(), title, rev, e.getValue().size());
                })
                .toList();

        List<RevenueReportResponse.RevenueByTheater> byTheaterList = byTheater.entrySet().stream()
                .map(e -> {
                    BigDecimal rev = e.getValue().stream()
                            .map(r -> r.getShowtime().getPricePerSeat().multiply(BigDecimal.valueOf(r.getReservedSeats().size())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    String name = e.getValue().getFirst().getShowtime().getTheater().getName();
                    return new RevenueReportResponse.RevenueByTheater(e.getKey(), name, rev, e.getValue().size());
                })
                .toList();

        return new RevenueReportResponse(totalRevenue, byMovieList, byTheaterList);
    }
}
