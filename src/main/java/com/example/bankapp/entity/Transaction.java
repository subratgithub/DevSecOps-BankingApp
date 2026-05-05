package com.example.bankapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long fromAccountId;
    
    @Column(nullable = true)
    private Long toAccountId;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private String type; // DEPOSIT, WITHDRAW, TRANSFER
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private Double balanceAfter;
    
    public Transaction() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Transaction(Long fromAccountId, Double amount, String type, Double balanceAfter) {
        this.fromAccountId = fromAccountId;
        this.amount = amount;
        this.type = type;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFromAccountId() {
        return fromAccountId;
    }
    
    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
    
    public Long getToAccountId() {
        return toAccountId;
    }
    
    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Double getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
}

