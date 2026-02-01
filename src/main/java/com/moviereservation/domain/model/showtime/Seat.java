package com.moviereservation.domain.model.showtime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    private Long id;
    private Theater theater;
    private String rowLetter;
    private Integer seatNumber;
}
