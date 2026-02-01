package com.moviereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {
        UserDetailsServiceAutoConfiguration.class,
        RedisAutoConfiguration.class
})
public class MovieReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieReservationApplication.class, args);
    }
}
