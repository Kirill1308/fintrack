package com.popov.fintrack.transaction.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.TransactionService;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.utills.SpecificationUtils;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getFilteredTransactions(FilterDTO filters, Pageable pageable) {
        Specification<Transaction> spec = Specification.where(null);

        spec = SpecificationUtils.applyWalletFilter(spec, filters.getWalletIds());
        spec = SpecificationUtils.applyCategoryFilter(spec, filters.getCategories());
        spec = SpecificationUtils.applyDateRangeFilter(spec, filters.getDateRange());
        spec = SpecificationUtils.applyAmountRangeFilter(spec, filters.getAmountRange());
        spec = SpecificationUtils.applyNoteFilter(spec, filters.getNote());

        return transactionRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    @Override
    @Transactional
    public Transaction update(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Wallet wallet = walletService.getWalletById(transaction.getWalletId());

        return wallet.getUser().getId().equals(userId);
    }
}
