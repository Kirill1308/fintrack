package com.popov.fintrack.transaction;

import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    Page<Transaction> getFilteredTransactions(FilterDTO filters, Pageable pageable);

    Transaction getTransaction(Long id);

    Transaction createTransaction(Transaction transaction);

    Transaction updateTransaction(Transaction transaction);

    void deleteTransaction(Long transactionId);
}
