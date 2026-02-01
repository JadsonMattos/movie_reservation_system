package com.moviereservation.application.dto.admin;

import java.math.BigDecimal;

public record CapacityReportResponse(
        Long showtimeId,
        String movieTitle,
        String theaterName,
        int totalSeats,
        int reservedSeats,
        int availableSeats,
        BigDecimal occupancyPercent
) {}
