package com.expensetracker.insights;

import com.expensetracker.expense.Expense;
import com.expensetracker.expense.ExpenseService;
import com.expensetracker.insights.dto.InsightsResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spending analytics. Preserves the exact rules from the original InsightsService:
 *  - total spending
 *  - category-wise breakdown
 *  - highest spending category
 *  - "smart insight": flags any single category above 50% of total.
 */
@Service
public class InsightsService {

    private final ExpenseService expenseService;

    public InsightsService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public InsightsResponse getInsights(String username) {
        List<Expense> expenses = expenseService.getRawExpenses(username);

        if (expenses.isEmpty()) {
            return new InsightsResponse(0.0, "N/A", 0.0, Map.of(), "No expenses available.");
        }

        double total = 0;
        Map<String, Double> categoryMap = new HashMap<>();
        for (Expense e : expenses) {
            BigDecimal amount = e.getAmount();
            double value = amount.doubleValue();
            total += value;
            categoryMap.merge(e.getCategory(), value, Double::sum);
        }

        String highestCategory = "";
        double highestAmount = 0;
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            if (entry.getValue() > highestAmount) {
                highestAmount = entry.getValue();
                highestCategory = entry.getKey();
            }
        }

        String smartInsight = "Your spending is well balanced. Good job!";
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            double percent = (entry.getValue() / total) * 100;
            if (percent > 50) {
                smartInsight = String.format(
                        "High spending alert! %s accounts for %.2f%% of total expenses.",
                        entry.getKey(), percent);
                break;
            }
        }

        return new InsightsResponse(total, highestCategory, highestAmount, categoryMap, smartInsight);
    }
}
