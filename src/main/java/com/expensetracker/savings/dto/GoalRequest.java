package com.expensetracker.savings.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record GoalRequest(
        @NotBlank(message = "productName is required") String productName,
        @Positive(message = "targetAmount must be greater than 0") double targetAmount,
        String productLink
) {}
