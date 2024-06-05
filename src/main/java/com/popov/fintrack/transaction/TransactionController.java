package com.popov.fintrack.transaction;

import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.dto.PaginatedResponse;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.web.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public PaginatedResponse<TransactionDTO> getFilteredTransactions(
            @RequestBody FilterDTO filters,
            @RequestParam int page,
            @RequestParam int limit) {

        Pageable pageable = PageRequest.of(page, limit);

        Page<Transaction> transactions = transactionService.getFilteredTransactions(filters, pageable);
        List<TransactionDTO> transactionDTOs = transactionMapper.toDto(transactions.getContent());

        return new PaginatedResponse<>(
                transactionDTOs,
                transactions.getTotalPages(),
                transactions.getTotalElements(),
                transactions.getNumber()
        );
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("@customSecurityExpression.isOwnerOfTransaction(#transactionId)")
    public TransactionDTO getByTransactionById(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);
        return transactionMapper.toDto(transaction);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#transactionDTO.walletId)")
    public TransactionDTO updateTransaction(@RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        Transaction updatedTransaction = transactionService.update(transaction);
        return transactionMapper.toDto(updatedTransaction);
    }

    @DeleteMapping("/{transactionId}")
    @PreAuthorize("@customSecurityExpression.isOwnerOfTransaction(#transactionId)")
    public void deleteTransaction(@PathVariable Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }
}
