package com.moviereservation.domain.port;

import java.time.Duration;
import java.util.List;

/**
 * Port for distributed seat locking during reservation.
 */
public interface SeatLockPort {

    boolean tryLock(Long showtimeId, List<Long> seatIds, Duration duration);

    void unlock(Long showtimeId, List<Long> seatIds);

    void unlockAll(Long showtimeId, List<Long> seatIds);
}
