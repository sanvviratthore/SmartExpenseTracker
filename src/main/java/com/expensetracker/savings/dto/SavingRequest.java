package com.expensetracker.savings.dto;

import jakarta.validation.constraints.Positive;

public record SavingRequest(
        @Positive(message = "amount must be greater than 0") double amount
) {}
