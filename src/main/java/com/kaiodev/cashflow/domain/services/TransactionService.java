package com.kaiodev.cashflow.domain.services;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

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
    TransactionRepository repository;

    public Page<TransactionDTO> getUserTransactions(String userId, String transactionDescription, Pageable pageable) {
        var response = repository.findByUserIdAndDescriptionContainingOrderByCreatedAtDesc(userId, transactionDescription, pageable);

        return response;
    }

    public Page<TransactionDTO> getUserTransactions(String userId, Pageable pageable) {
        var response = repository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return response;
    }

    public LastTransactionsDTO getLastTransactions(String userId) {
        Transaction lastCreditTransaction = this
                .findLastTransactionByType(repository.findAllByUserId(userId), TransactionType.credit);
        Transaction lastDebitTransaction = this
                .findLastTransactionByType(repository.findAllByUserId(userId), TransactionType.debit);

        return new LastTransactionsDTO(lastCreditTransaction, lastDebitTransaction);
    }

    public BalanceDTO getUserBalance(String userId) {
        List<Transaction> userTransactions = repository.findAllByUserId(userId);

        final double[] credits = { 0.0 };
        final double[] debits = { 0.0 };

        userTransactions.stream().forEach(transaction -> {
            if (transaction.getType() == TransactionType.credit) {
                credits[0] += transaction.getAmount();
            } else {
                debits[0] += transaction.getAmount();
            }
        });

        return new BalanceDTO(credits[0], debits[0], credits[0] - debits[0]);
    }

    public Transaction createTransaction(Transaction data, User user) throws BusinessException {
        BalanceDTO balance = this.getUserBalance(user.getId());

        if (data.getType() == TransactionType.debit && data.getAmount() > balance.balance())
            throw new BusinessException("Saldo insuficiente");
        else if (data.getDescription().length() < 2) {
            throw new BusinessException("A descrição deve possuir mais de dois caracteres");
        } else if (data.getAmount() < 0) {
            throw new BusinessException("Valor de transação deve ser maior que zero");
        } else if (data.getCategory().length() < 2) {
            throw new BusinessException("A categoria deve possuir mais de dois caracteres");
        }

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
