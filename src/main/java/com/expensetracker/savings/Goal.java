package com.expensetracker.savings;

import jakarta.persistence.*;

/**
 * Wishlist goal. Replaces the "GOAL|product|price|link" line from the savings file.
 */
@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private double targetAmount;

    private String productLink;

    protected Goal() {
        // required by JPA
    }

    public Goal(String username, String productName, double targetAmount, String productLink) {
        this.username = username;
        this.productName = productName;
        this.targetAmount = targetAmount;
        this.productLink = productLink;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProductName() {
        return productName;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public String getProductLink() {
        return productLink;
    }
}
