package com.moviereservation.application.dto.admin;

import java.math.BigDecimal;
import java.util.List;

public record RevenueReportResponse(
        BigDecimal totalRevenue,
        List<RevenueByMovie> byMovie,
        List<RevenueByTheater> byTheater
) {

    public record RevenueByMovie(Long movieId, String movieTitle, BigDecimal revenue, long reservationCount) {}
    public record RevenueByTheater(Long theaterId, String theaterName, BigDecimal revenue, long reservationCount) {}
}
