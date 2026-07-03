package com.expensetracker.insights.dto;

import java.util.Map;

public record InsightsResponse(
        double totalSpending,
        String highestSpendingCategory,
        double highestSpendingAmount,
        Map<String, Double> categoryBreakdown,
        String smartInsight
) {}
