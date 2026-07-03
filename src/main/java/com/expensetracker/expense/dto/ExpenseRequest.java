package com.expensetracker.expense.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ExpenseRequest(
        @NotBlank(message = "title is required") String title,
        @Positive(message = "amount must be greater than 0") double amount,
        @NotBlank(message = "category is required") String category
) {}
