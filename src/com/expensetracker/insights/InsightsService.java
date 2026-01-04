package com.expensetracker.insights;
import com.expensetracker.expense.Expense;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsightsService {

    private List<Expense> expenses;

    public InsightsService(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public double getTotalSpending() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmt();
        }
        return total;
    }

    public Map<String, Double> getCategoryWiseSpending() {
        Map<String, Double> catMap = new HashMap<>();
        for (Expense e : expenses) {
            catMap.put(
                    e.getCategory(),
                    catMap.getOrDefault(e.getCategory(), 0.0) + e.getAmt()
            );
        }
        return catMap;
    }

    public void highestSpendingCategory() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }

        Map<String, Double> catMap = getCategoryWiseSpending();

        String maxCat = "";
        double maxAmt = 0;

        for (Map.Entry<String, Double> entry : catMap.entrySet()) {
            if (entry.getValue() > maxAmt) {
                maxAmt = entry.getValue();
                maxCat = entry.getKey();
            }
        }

        System.out.println("Highest spending category: "
                + maxCat + " (₹" + maxAmt + ")");
    }

    public void smartInsight() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }

        Map<String, Double> catMap = getCategoryWiseSpending();
        double total = getTotalSpending();

        for (Map.Entry<String, Double> entry : catMap.entrySet()) {
            double percentage = (entry.getValue() / total) * 100;

            if (percentage > 50) {
                System.out.println("⚠️ High spending alert!");
                System.out.println(entry.getKey() + " accounts for "
                        + String.format("%.2f", percentage)
                        + "% of your total expenses.");
                return;
            }
        }

        System.out.println("Your spending is well balanced. Good job!");
    }
}
