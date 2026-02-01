package com.moviereservation.domain.model.movie;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    private Long id;
    private String name;
}
