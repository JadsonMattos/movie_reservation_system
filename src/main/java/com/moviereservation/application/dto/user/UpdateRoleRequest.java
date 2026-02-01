package com.moviereservation.application.dto.user;

import com.moviereservation.domain.model.user.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(@NotNull UserRole role) {}
