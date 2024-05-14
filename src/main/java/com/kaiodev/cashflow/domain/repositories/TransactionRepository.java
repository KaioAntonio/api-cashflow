package com.kaiodev.cashflow.domain.repositories;

import com.kaiodev.cashflow.domain.transaction.Transaction;
import com.kaiodev.cashflow.domain.transaction.TransactionDTO;
import com.kaiodev.cashflow.domain.transaction.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findAllByUserId(String userId);

    Page<TransactionDTO> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    Page<TransactionDTO> findByUserIdAndDescriptionContainingOrderByCreatedAtDesc(
            String userId,
            String transactionDescription,
            Pageable pageable);

    List<Transaction> findAllByType(TransactionType type);
}
