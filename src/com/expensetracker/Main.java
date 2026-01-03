package com.expensetracker;
import com.expensetracker.auth.AuthService;
import com.expensetracker.expense.ExpenseService;
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

        boolean isAuthenticated = false;

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

        // 🚨 Stop app if auth failed
        if (!isAuthenticated) {
            System.out.println("Exiting application...");
            return;
        }

        ExpenseService expenseService = new ExpenseService();

        while (true) {
            System.out.println("\n1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Exit");

            int choiceE = Integer.parseInt(sc.nextLine());

            switch (choiceE) {
                case 1 -> expenseService.addExpense(sc);
                case 2 -> expenseService.viewExpenses();
                case 3 -> {
                    System.out.println("Goodbye 👋");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
