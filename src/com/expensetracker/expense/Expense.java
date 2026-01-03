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
        return title+ "," + "amt" + "," + category + "," + date;
    }

    //how text becomes objects
    public static Expense fromFileString(String line){ //factory method
        String[] parts=line.split(",");
        return new Expense(
                parts[0],
                Double.parseDouble(parts[1]),
                parts[2],
                LocalDate.parse(parts[3])
        );
    }

    @Override
    public String toString(){
        return date + " | " + title + " | ₹" + amt + " | " + category;
    }
}
