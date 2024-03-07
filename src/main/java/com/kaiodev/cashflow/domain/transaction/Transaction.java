package com.kaiodev.cashflow.domain.transaction;

import java.time.LocalDateTime;

import com.kaiodev.cashflow.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "transactions")
@Entity(name = "transactions")
@NoArgsConstructor
@Getter
//@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double amount;
    private String description;
    private String category;
    private TransactionType type;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction(Double amount, String description, String category, TransactionType type, LocalDateTime createdAt,
            User user) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.type = type;
        this.createdAt = createdAt;
        this.user = user;
    }
}
