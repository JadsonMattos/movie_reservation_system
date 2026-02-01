package com.moviereservation.application.dto.movie;

import com.moviereservation.application.dto.showtime.ShowtimeResponse;

import java.util.List;

public record MovieWithShowtimesResponse(
        Long id,
        String title,
        String description,
        String posterUrl,
        String genreName,
        Integer durationMinutes,
        List<ShowtimeResponse> showtimes
) {}
