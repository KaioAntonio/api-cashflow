package com.kaiodev.cashflow.domain.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.kaiodev.cashflow.domain.services.validator.TransactionValidator;
import com.kaiodev.cashflow.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kaiodev.cashflow.domain.repositories.TransactionRepository;
import com.kaiodev.cashflow.domain.transaction.LastTransactionsDTO;
import com.kaiodev.cashflow.domain.transaction.Transaction;
import com.kaiodev.cashflow.domain.transaction.TransactionDTO;
import com.kaiodev.cashflow.domain.transaction.TransactionType;
import com.kaiodev.cashflow.domain.user.BalanceDTO;
import com.kaiodev.cashflow.domain.user.User;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private TransactionValidator transactionValidator;

    public Page<TransactionDTO> getUserTransactions(String userId, String transactionDescription, Pageable pageable) {
        return repository.findByUserIdAndDescriptionContainingOrderByCreatedAtDesc(userId, transactionDescription, pageable);
    }

    public Page<TransactionDTO> getUserTransactions(String userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public LastTransactionsDTO getLastTransactions(String userId) {
        Transaction lastCreditTransaction = this
                .findLastTransactionByType(repository.findAllByUserId(userId), TransactionType.CREDIT);
        Transaction lastDebitTransaction = this
                .findLastTransactionByType(repository.findAllByUserId(userId), TransactionType.DEBIT);

        return new LastTransactionsDTO(lastCreditTransaction, lastDebitTransaction);
    }

    public BalanceDTO calculateUserBalance(String userId) {
        List<Transaction> userTransactions = repository.findAllByUserId(userId);

        double totalCredits = userTransactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.CREDIT)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalDebits = userTransactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.DEBIT)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double balance = totalCredits - totalDebits;

        return new BalanceDTO(totalCredits, totalDebits, balance);
    }

    public Transaction createTransaction(Transaction data, User user) throws BusinessException {
        BalanceDTO balance = this.calculateUserBalance(user.getId());

        transactionValidator.createTransactionValidator(data, balance);

        Transaction transaction = new Transaction(data.getAmount(), data.getDescription(), data.getCategory(),
                data.getType(),
                LocalDateTime.now(), user);

        repository.save(transaction);

        return transaction;
    }

    private Transaction findLastTransactionByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(transaction -> type.equals(transaction.getType()))
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(new Transaction());
    }
}
