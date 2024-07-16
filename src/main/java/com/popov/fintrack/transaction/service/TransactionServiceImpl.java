package com.popov.fintrack.transaction.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.TransactionService;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.user.service.UserServiceImpl;
import com.popov.fintrack.utills.SpecificationUtils;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserServiceImpl userService;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> getFilteredTransactions(FilterDTO filters, Pageable pageable) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, null);
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
    public Transaction createTransaction(Transaction transaction) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        User user = userService.getUserById(userId);
        transaction.setOwner(user);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
