package com.expensetracker.savings.dto;

import com.expensetracker.savings.Saving;

import java.time.LocalDate;

public record SavingResponse(Long id, double amount, LocalDate date) {
    public static SavingResponse from(Saving s) {
        return new SavingResponse(s.getId(), s.getAmount(), s.getDate());
    }
}
