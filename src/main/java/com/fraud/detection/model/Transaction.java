
package com.fraud.detection.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String merchant;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int transactionHour;

    private boolean flaggedFraud;
    private String fraudReason;
    private LocalDateTime createdAt;

    public Transaction() {
    }

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTransactionHour() {
        return transactionHour;
    }

    public void setTransactionHour(int transactionHour) {
        this.transactionHour = transactionHour;
    }

    public boolean isFlaggedFraud() {
        return flaggedFraud;
    }

    public void setFlaggedFraud(boolean flaggedFraud) {
        this.flaggedFraud = flaggedFraud;
    }

    public String getFraudReason() {
        return fraudReason;
    }

    public void setFraudReason(String fraudReason) {
        this.fraudReason = fraudReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}