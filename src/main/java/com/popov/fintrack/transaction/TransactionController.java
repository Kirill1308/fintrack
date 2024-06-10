package com.popov.fintrack.transaction;

import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.dto.PaginatedResponse;
import com.popov.fintrack.transaction.dto.TransactionDTO;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "API for managing transactions")
public class TransactionController {

    private final WalletService walletService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @PostMapping
    @PreAuthorize("@customSecurityExpression.hasAccessToWallets(#filters.walletIds)")
    @Operation(summary = "Get filtered transactions", description = "Retrieve transactions based on filters and pagination parameters")
    @ApiResponses(value = {
            @ApiResponse(description = "Successful retrieval of transactions", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = PaginatedResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public PaginatedResponse<TransactionDTO> getFilteredTransactions(
            @RequestBody @Parameter(description = "Filter criteria for transactions") FilterDTO filters,
            @RequestParam @Parameter(description = "Page number for pagination") int page,
            @RequestParam @Parameter(description = "Page size for pagination") int limit) {

        log.info("Request to get filtered transactions with filters: {}, page: {}, limit: {}", filters, page, limit);
        Pageable pageable = PageRequest.of(page, limit);

        Page<Transaction> transactions = transactionService.getFilteredTransactions(filters, pageable);
        List<TransactionDTO> transactionDTOs = transactionMapper.toDto(transactions.getContent());

        PaginatedResponse<TransactionDTO> response = new PaginatedResponse<>(
                transactionDTOs,
                transactions.getTotalPages(),
                transactions.getTotalElements(),
                transactions.getNumber()
        );
        log.info("Retrieved filtered transactions: {}", response);
        return response;
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("@customSecurityExpression.isOwnerOfTransaction(#transactionId)")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Transaction Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public TransactionDTO getTransactionById(@PathVariable @Parameter(description = "ID of the transaction to retrieve") Long transactionId) {
        log.info("Request to get transaction by ID: {}", transactionId);
        Transaction transaction = transactionService.getTransaction(transactionId);
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        log.info("Retrieved transaction: {}", transactionDTO);
        return transactionDTO;
    }

    @PostMapping("/{walletId}")
    @PreAuthorize("@customSecurityExpression.hasAccessToWallet(#walletId)")
    @Operation(summary = "Create a new transaction", description = "Create a new transaction for a specific wallet")
    @ApiResponses(value = {
            @ApiResponse(description = "Transaction Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public TransactionDTO createTransaction(@PathVariable @Parameter(description = "ID of the wallet to create the transaction for") Long walletId,
                                            @RequestBody @Parameter(description = "Transaction details") TransactionDTO transactionDTO) {
        log.info("Request to create transaction for wallet ID: {}, transaction details: {}", walletId, transactionDTO);
        Wallet wallet = walletService.getWalletById(walletId);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction.setWallet(wallet);
        Transaction createdTransaction = transactionService.createTransaction(transaction);
        TransactionDTO createdTransactionDTO = transactionMapper.toDto(createdTransaction);
        log.info("Created transaction: {}", createdTransactionDTO);
        return createdTransactionDTO;
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.isOwnerOfTransaction(#transactionDTO.id)")
    @Operation(summary = "Update a transaction", description = "Update an existing transaction")
    @ApiResponses(value = {
            @ApiResponse(description = "Transaction Details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public TransactionDTO updateTransaction(@RequestBody @Parameter(description = "Updated transaction details") TransactionDTO transactionDTO) {
        log.info("Request to update transaction: {}", transactionDTO);
        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);
        TransactionDTO updatedTransactionDTO = transactionMapper.toDto(updatedTransaction);
        log.info("Updated transaction: {}", updatedTransactionDTO);
        return updatedTransactionDTO;
    }

    @DeleteMapping("/{transactionId}")
    @PreAuthorize("@customSecurityExpression.isOwnerOfTransaction(#transactionId)")
    @Operation(summary = "Delete a transaction", description = "Delete a transaction by its ID")
    @ApiResponses(value = {
            @ApiResponse(description = "Transaction deleted successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public void deleteTransaction(@PathVariable @Parameter(description = "ID of the transaction to delete") Long transactionId) {
        log.info("Request to delete transaction by ID: {}", transactionId);
        transactionService.deleteTransaction(transactionId);
        log.info("Deleted transaction with ID: {}", transactionId);
    }
}
