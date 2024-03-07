package com.kaiodev.cashflow.domain.transaction;

import java.time.LocalDateTime;

public record TransactionDTO(
        Double amount, String description,
        String category, TransactionType type,
        LocalDateTime createdAt) {
}
