package com.expensetracker.savings.dto;

import com.expensetracker.savings.Goal;

public record GoalResponse(Long id, String productName, double targetAmount, String productLink) {
    public static GoalResponse from(Goal g) {
        return new GoalResponse(g.getId(), g.getProductName(), g.getTargetAmount(), g.getProductLink());
    }
}
