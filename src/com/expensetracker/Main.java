package com.expensetracker;

import com.expensetracker.auth.AuthService;
import com.expensetracker.expense.ExpenseService;
import com.expensetracker.insights.InsightsService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        System.out.println("1. Register");
        System.out.println("2. Login");
        int choice = sc.nextInt();
        sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        boolean isAuthenticated;

        if (choice == 1) {
            isAuthenticated = auth.register(username, pass);
            System.out.println(isAuthenticated
                    ? "Registration successful"
                    : "User already exists");
        } else {
            isAuthenticated = auth.login(username, pass);
            System.out.println(isAuthenticated
                    ? "Login successful"
                    : "Invalid credentials");
        }

        if (!isAuthenticated) {
            System.out.println("Exiting application...");
            return;
        }

        ExpenseService expenseService = new ExpenseService();

        while (true) {
            System.out.println("\n====== Smart Expense Tracker ======");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Total Spending");
            System.out.println("4. Highest Spending Category");
            System.out.println("5. Smart Insight");
            System.out.println("6. Exit");

            int choiceE = Integer.parseInt(sc.nextLine());

            InsightsService insights = new InsightsService(expenseService.getExpenses());

            switch (choiceE) {
                case 1 -> expenseService.addExpense(sc);
                case 2 -> expenseService.viewExpenses();
                case 3 -> System.out.println(
                        "Total Spending: ₹" + insights.getTotalSpending()
                );
                case 4 -> insights.highestSpendingCategory();
                case 5 -> insights.smartInsight();
                case 6 -> {
                    System.out.println("Goodbye 👋");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}


