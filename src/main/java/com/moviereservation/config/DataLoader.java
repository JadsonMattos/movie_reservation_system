package com.moviereservation.config;

import com.moviereservation.application.port.out.PasswordEncoderPort;
import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.model.user.UserRole;
import com.moviereservation.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Created initial admin user: admin@example.com / admin123");
        }
    }
}
