package com.moviereservation.service;

import com.moviereservation.application.dto.showtime.ShowtimeRequest;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.application.service.ShowtimeService;
import com.moviereservation.domain.model.movie.Genre;
import com.moviereservation.domain.model.movie.Movie;
import com.moviereservation.domain.model.showtime.Showtime;
import com.moviereservation.domain.model.showtime.Theater;
import com.moviereservation.domain.port.MovieRepositoryPort;
import com.moviereservation.domain.port.ShowtimeRepositoryPort;
import com.moviereservation.domain.port.TheaterRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepositoryPort showtimeRepository;
    @Mock
    private MovieRepositoryPort movieRepository;
    @Mock
    private TheaterRepositoryPort theaterRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Genre genre;
    private Movie movie;
    private Theater theater;
    private Showtime showtime;
    private Instant futureTime;

    @BeforeEach
    void setUp() {
        futureTime = Instant.now().plus(1, ChronoUnit.DAYS);
        genre = Genre.builder().id(1L).name("Action").build();
        movie = Movie.builder().id(1L).title("Movie").genre(genre).durationMinutes(120).build();
        theater = Theater.builder().id(1L).name("Theater 1").build();
        showtime = Showtime.builder()
                .id(1L)
                .movie(movie)
                .theater(theater)
                .startTime(futureTime)
                .pricePerSeat(BigDecimal.TEN)
                .build();
    }

    @Test
    void update_shouldUpdateShowtime() {
        ShowtimeRequest request = new ShowtimeRequest(1L, 1L, futureTime.plus(1, ChronoUnit.DAYS), BigDecimal.valueOf(15));
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(theaterRepository.findById(1L)).thenReturn(Optional.of(theater));
        when(showtimeRepository.findByTheaterId(1L)).thenReturn(List.of());
        when(showtimeRepository.save(any(Showtime.class))).thenAnswer(inv -> inv.getArgument(0));

        var response = showtimeService.update(1L, request);

        assertThat(response.pricePerSeat()).isEqualByComparingTo(BigDecimal.valueOf(15));
        verify(showtimeRepository).save(any(com.moviereservation.domain.model.showtime.Showtime.class));
    }

    @Test
    void update_shouldThrowWhenShowtimeNotFound() {
        ShowtimeRequest request = new ShowtimeRequest(1L, 1L, futureTime, BigDecimal.TEN);
        when(showtimeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showtimeService.update(999L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Showtime");
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        when(showtimeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> showtimeService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
