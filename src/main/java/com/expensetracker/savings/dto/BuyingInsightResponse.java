package com.expensetracker.savings.dto;

public record BuyingInsightResponse(
        String productName,
        double targetAmount,
        String productLink,
        double savedPercent,
        boolean canBuy
) {}
