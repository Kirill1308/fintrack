package com.popov.fintrack.transaction;

import com.popov.fintrack.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);

    List<Transaction> findAll(Specification<Transaction> spec);

    boolean existsByIdAndOwnerId(Long transactionId, Long userId);
}
