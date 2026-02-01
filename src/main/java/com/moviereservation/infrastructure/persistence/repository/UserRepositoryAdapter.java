package com.moviereservation.infrastructure.persistence.repository;

import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.port.UserRepositoryPort;
import com.moviereservation.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.moviereservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper mapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        entity = userRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(mapper::toDomain);
    }
}
