package com.expensetracker.expense;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Expense entity. Replaces the comma-delimited line in data/expenses_&lt;user&gt;.txt.
 * The "username" column preserves the original per-user data isolation.
 */
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate date;

    protected Expense() {
        // required by JPA
    }

    public Expense(String username, String title, double amount, String category, LocalDate date) {
        this.username = username;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }
}
