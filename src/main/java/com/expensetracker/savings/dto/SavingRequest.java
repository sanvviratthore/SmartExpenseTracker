package com.expensetracker.savings.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record SavingRequest(
        @Positive(message = "amount must be greater than 0") BigDecimal amount
) {}