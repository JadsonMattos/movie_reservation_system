package com.moviereservation.domain.model.reservation;

import com.moviereservation.domain.model.showtime.Seat;
import com.moviereservation.domain.model.showtime.Showtime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservedSeat {

    private Long id;
    private Reservation reservation;
    private Seat seat;
    private Showtime showtime;
}
