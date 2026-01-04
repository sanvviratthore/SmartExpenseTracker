package com.expensetracker.expense;
import java.time.LocalDate;

public class Expense {
    private String title;
    private double amt;
    private String category;
    private LocalDate date;

    public Expense(String title, double amt, String category, LocalDate date){
        this.title=title;
        this.amt=amt;
        this.category=category;
        this.date=date;
    }

    //how objects become text
    public String toFileString(){
        return title+ "," + amt + "," + category + "," + date;
    }

    //how text becomes objects
    public static Expense fromFileString(String line) {
        try {
            String[] parts = line.split(",");

            String title = parts[0];
            double amt = Double.parseDouble(parts[1]);
            String category = parts[2];
            LocalDate date = LocalDate.parse(parts[3]);

            return new Expense(title, amt, category, date);
        } catch (Exception e) {
            System.out.println("Skipping corrupted line: " + line);
            return null;
        }
    }

    @Override
    public String toString(){
        return date + " | " + title + " | ₹" + amt + " | " + category;
    }

    public double getAmt() {
        return amt;
    }

    public String getCategory() {
        return category;
    }

}
