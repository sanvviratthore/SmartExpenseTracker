package com.expensetracker.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ExpenseRequest(
        @NotBlank(message = "title is required") String title,
        @Positive(message = "amount must be greater than 0") BigDecimal amount,
        @NotBlank(message = "category is required") String category
) {}
