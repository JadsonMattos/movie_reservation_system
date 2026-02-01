package com.moviereservation.domain.model.reservation;

import com.moviereservation.domain.model.showtime.Showtime;
import com.moviereservation.domain.model.user.User;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity - Reservation aggregate root.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    private Long id;
    private User user;
    private Showtime showtime;
    private ReservationStatus status;
    private Instant createdAt;
    @Builder.Default
    private List<ReservedSeat> reservedSeats = new ArrayList<>();

    public void cancel() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        if (showtime.isInThePast()) {
            throw new IllegalStateException("Cannot cancel reservation for past showtime");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public boolean belongsTo(User user) {
        if (this.user == null || user == null) return false;
        return this.user.getId().equals(user.getId());
    }
}
