package com.moviereservation.application.port.out;

public interface TokenGeneratorPort {

    String generate(Long userId, String email, String role);
}
