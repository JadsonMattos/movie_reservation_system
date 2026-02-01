package com.moviereservation.infrastructure.adapter;

import com.moviereservation.domain.port.SeatLockPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * No-op implementation when Redis is disabled.
 * Relies on DB unique constraint for overbooking prevention.
 */
@Component
@ConditionalOnProperty(name = "app.redis.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpSeatLockAdapter implements SeatLockPort {

    @Override
    public boolean tryLock(Long showtimeId, List<Long> seatIds, Duration duration) {
        return true;
    }

    @Override
    public void unlock(Long showtimeId, List<Long> seatIds) {
        // no-op
    }

    @Override
    public void unlockAll(Long showtimeId, List<Long> seatIds) {
        // no-op
    }
}
