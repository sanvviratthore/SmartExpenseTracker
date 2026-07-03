package com.expensetracker.auth.dto;

/** Returned on successful login/registration. */
public record AuthResponse(String token, String username) {}
