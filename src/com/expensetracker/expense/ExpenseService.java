package com.expensetracker.expense;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ExpenseService {
    private List<Expense> expenses;

    public ExpenseService(){
        expenses = ExpenseStorage.loadExpenses();
    }

    public void addExpense(Scanner sc){
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Amount: ");
        double amt=Double.parseDouble(sc.nextLine());
        System.out.print("Category: ");
        String category=sc.nextLine();
        Expense expense = new Expense(title, amt, category, LocalDate.now());
        expenses.add(expense);

        ExpenseStorage.saveExpenses(expenses);
        System.out.println("Expenses added!");
    }

    public void viewExpenses(){
        if(expenses.isEmpty()){
            System.out.println("No expenses yet.");
            return;
        }

        System.out.println("\n---- Your Expenses ----");
        for (Expense e : expenses){
            System.out.println(e);
        }
    }
}
