package com.expensetracker.savings.dto;

import com.expensetracker.savings.Goal;
import java.math.BigDecimal;

public record GoalResponse(Long id, String productName, BigDecimal targetAmount, String productLink) {
    public static GoalResponse from(Goal g) {
        return new GoalResponse(g.getId(), g.getProductName(), g.getTargetAmount(), g.getProductLink());
    }
}