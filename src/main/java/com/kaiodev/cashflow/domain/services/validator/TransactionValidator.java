package com.kaiodev.cashflow.domain.services.validator;

import com.kaiodev.cashflow.domain.transaction.Transaction;
import com.kaiodev.cashflow.domain.transaction.TransactionType;
import com.kaiodev.cashflow.domain.user.BalanceDTO;
import com.kaiodev.cashflow.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator {

    public void createTransactionValidator(Transaction data, BalanceDTO balance) throws BusinessException {
        if (data.getType() == TransactionType.DEBIT && data.getAmount() > balance.balance())
            throw new BusinessException("Saldo insuficiente");
        else if (data.getDescription().length() < 2) {
            throw new BusinessException("A descrição deve possuir mais de dois caracteres");
        } else if (data.getAmount() < 0) {
            throw new BusinessException("Valor de transação deve ser maior que zero");
        } else if (data.getCategory().length() < 2) {
            throw new BusinessException("A categoria deve possuir mais de dois caracteres");
        }
    }
}
