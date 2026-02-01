package com.moviereservation.controller;

import com.moviereservation.application.dto.admin.CapacityReportResponse;
import com.moviereservation.application.dto.admin.RevenueReportResponse;
import com.moviereservation.application.dto.reservation.ReservationResponse;
import com.moviereservation.application.service.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Reports", description = "Reservations, capacity and revenue reports")
public class AdminReportController {

    private final AdminReportService reportService;

    @GetMapping("/reservations")
    @Operation(summary = "List all reservations (with optional filters)")
    public List<ReservationResponse> listReservations(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return reportService.findAllReservations(movieId, userId, fromDate, toDate);
    }

    @GetMapping("/showtimes/{showtimeId}/capacity")
    @Operation(summary = "Get showtime capacity report")
    public CapacityReportResponse getCapacity(@PathVariable Long showtimeId) {
        return reportService.getShowtimeCapacity(showtimeId);
    }

    @GetMapping("/revenue")
    @Operation(summary = "Get revenue report")
    public RevenueReportResponse getRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return reportService.getRevenue(fromDate, toDate);
    }
}
