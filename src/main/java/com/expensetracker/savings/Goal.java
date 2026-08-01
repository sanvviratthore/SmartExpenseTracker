package com.expensetracker.savings;

import java.math.BigDecimal;

import jakarta.persistence.*;

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

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    private String productLink;

    protected Goal() {
    }

    public Goal(String username, String productName, BigDecimal targetAmount, String productLink) {
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

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public String getProductLink() {
        return productLink;
    }
}