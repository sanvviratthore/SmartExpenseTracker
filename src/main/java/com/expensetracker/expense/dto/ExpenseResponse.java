package com.expensetracker.expense.dto;

import com.expensetracker.expense.Expense;

import java.time.LocalDate;
import java.math.BigDecimal;

public record ExpenseResponse(
        Long id,
        String title,
        BigDecimal amount,
        String category,
        LocalDate date
) {
    public static ExpenseResponse from(Expense e) {
        return new ExpenseResponse(e.getId(), e.getTitle(), e.getAmount(), e.getCategory(), e.getDate());
    }
}