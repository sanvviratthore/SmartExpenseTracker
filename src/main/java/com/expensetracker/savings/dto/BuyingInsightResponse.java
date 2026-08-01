package com.expensetracker.savings.dto;

import java.math.BigDecimal;

public record BuyingInsightResponse(
        String productName,
        BigDecimal targetAmount,
        String productLink,
        BigDecimal savedPercent,
        boolean canBuy
) {}