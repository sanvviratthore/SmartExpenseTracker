package com.expensetracker.insights;
import com.expensetracker.expense.Expense;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsightsService {
    private List<Expense> expenses;
    public InsightsService(List<Expense> expenses){
        this.expenses=expenses;
    }

    public double getTotalSpending(){
        double tot=0;
        for(Expense e : expenses){
            tot += e.getAmt();
        }
        return tot;
    }

    public Map<String, Double> getCategoryWiseSpending(){
        Map<String, Double> catMap=new HashMap<>();
        for(Expense e : expenses){
            String cat=e.getCategory();
            double amt=e.getAmt();

            catMap.put(cat, catMap.getOrDefault(cat, 0.0)+amt);
        }
        return catMap;
    }

    public void highestSpendingCategory(){
        if(expenses.isEmpty()){
            System.out.println("No Expenses available.");
            return;
        }

        Map<String, Double> catMap=new HashMap<>();
        for(Expense e:expenses) {
            String cat = e.getCategory();
            double amt = e.getAmt();
            catMap.put(cat, catMap.getOrDefault(cat, 0.0) + amt);
        }
            String maxCat="";
            double maxAmt=0;

            for(Map.Entry<String, Double> it : catMap.entrySet()){
                if(it.getValue() > maxAmt){
                    maxAmt=it.getValue();
                    maxCat=it.getKey();
                }
            }
            System.out.println("Highest spending category: " + maxCat + "(₹" + maxAmt + ")");
    }

    public void smartInsight(){
        if(expenses.isEmpty()){
            System.out.println("No expenses available.");
            return;
        }
        Map<String, Double> catMap = new HashMap<>();
        double tot = 0;

        for (Expense e : expenses) {
            tot += e.getAmt();
            catMap.put(
                    e.getCategory(),
                    catMap.getOrDefault(e.getCategory(), 0.0) + e.getAmt()
            );
        }
        for(Map.Entry<String, Double> it : catMap.entrySet()){
            double perc = (it.getValue() / tot) *100;
            if(perc > 50){
                System.out.println("⚠️ High spending alert!");
                System.out.println(it.getKey()
                                  + " accounts for "
                                  + String.format("%.2f", perc)
                                  + "% of your total expenses.");
                return;
            }
        }
        System.out.println("Your spending is well balanced. Good job!");
    }
}
