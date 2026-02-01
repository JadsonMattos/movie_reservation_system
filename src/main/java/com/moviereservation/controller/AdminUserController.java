package com.moviereservation.controller;

import com.moviereservation.application.dto.user.UpdateRoleRequest;
import com.moviereservation.application.dto.user.UserResponse;
import com.moviereservation.application.exception.ResourceNotFoundException;
import com.moviereservation.domain.model.user.User;
import com.moviereservation.domain.port.UserRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin - Users", description = "Admin-only user management")
public class AdminUserController {

    private final UserRepositoryPort userRepository;

    @PatchMapping("/{id}/role")
    @Operation(summary = "Promote/demote user role (admin only)")
    public ResponseEntity<UserResponse> updateRole(@PathVariable Long id,
                                                   @Valid @RequestBody UpdateRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        user.setRole(request.role());
        user = userRepository.save(user);

        return ResponseEntity.ok(UserResponse.from(user));
    }
}
