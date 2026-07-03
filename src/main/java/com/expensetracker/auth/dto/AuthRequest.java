package com.expensetracker.auth.dto;

import jakarta.validation.constraints.NotBlank;

/** Login / registration payload. */
public record AuthRequest(
        @NotBlank(message = "username is required") String username,
        @NotBlank(message = "password is required") String password
) {}
