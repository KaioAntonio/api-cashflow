package com.kaiodev.cashflow.domain.transaction;

public record LastTransactionsDTO(Transaction lastCreditTransaction, Transaction lastDebitTransaction) {
    
}
