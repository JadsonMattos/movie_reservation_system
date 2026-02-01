package com.moviereservation.infrastructure.adapter;

import com.moviereservation.domain.port.SeatLockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.redis.enabled", havingValue = "true", matchIfMissing = false)
public class RedisSeatLockAdapter implements SeatLockPort {

    private static final String LOCK_PREFIX = "reservation:lock:";
    private static final Duration DEFAULT_LOCK_DURATION = Duration.ofSeconds(30);

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean tryLock(Long showtimeId, List<Long> seatIds, Duration duration) {
        Duration lockDuration = duration != null ? duration : DEFAULT_LOCK_DURATION;
        for (Long seatId : seatIds) {
            String key = lockKey(showtimeId, seatId);
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, "1", lockDuration);
            if (Boolean.FALSE.equals(acquired)) {
                unlock(showtimeId, seatIds.stream().takeWhile(sid -> !sid.equals(seatId)).toList());
                return false;
            }
        }
        return true;
    }

    @Override
    public void unlock(Long showtimeId, List<Long> seatIds) {
        seatIds.forEach(seatId -> redisTemplate.delete(lockKey(showtimeId, seatId)));
    }

    @Override
    public void unlockAll(Long showtimeId, List<Long> seatIds) {
        unlock(showtimeId, seatIds);
    }

    private String lockKey(Long showtimeId, Long seatId) {
        return LOCK_PREFIX + showtimeId + ":" + seatId;
    }
}
