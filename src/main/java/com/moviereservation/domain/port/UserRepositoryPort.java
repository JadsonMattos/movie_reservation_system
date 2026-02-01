package com.moviereservation.domain.port;

import com.moviereservation.domain.model.user.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findById(Long id);
}
