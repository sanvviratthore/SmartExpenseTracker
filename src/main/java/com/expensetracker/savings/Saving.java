package com.expensetracker.savings;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * A single savings deposit. Replaces the "SAVE|amount" line from the savings file.
 */
@Entity
@Table(name = "savings")
public class Saving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate date;

    protected Saving() {
        // required by JPA
    }

    public Saving(String username, double amount, LocalDate date) {
        this.username = username;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
