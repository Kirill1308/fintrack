package com.popov.fintrack.transaction.service;

import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.transaction.TransactionRepository;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.user.service.UserServiceImpl;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setOwner(user);
    }

    @Test
    void getFilteredTransactions_success_returnsPageOfTransactions() {
        FilterDTO filters = new FilterDTO();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = new PageImpl<>(List.of(transaction));

        when(transactionRepository.findAll((Specification<Transaction>) any(), any(Pageable.class))).thenReturn(page);

        Page<Transaction> result = transactionService.getFilteredTransactions(filters, pageable);
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository, times(1)).findAll((Specification<Transaction>) any(), any(Pageable.class));
    }

    @Test
    void getTransaction_isFound_returnsTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransaction(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void getTransaction_notFound_throwsRNFException() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransaction(1L));
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void createTransaction_success_returnsTransaction() {
        when(userService.getUserById(anyLong())).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getAuthenticatedUserId).thenReturn(1L);

            Transaction result = transactionService.createTransaction(transaction);

            assertNotNull(result);
            assertEquals(user, result.getOwner());
            verify(transactionRepository, times(1)).save(any(Transaction.class));
        }
    }

    @Test
    void updateTransaction_success_returnsTransaction() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionService.updateTransaction(transaction);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_success() {
        doNothing().when(transactionRepository).deleteById(1L);

        transactionService.deleteTransaction(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void isOwnerOfTransaction_true() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        boolean result = transactionService.isOwnerOfTransaction(1L, 1L);
        assertTrue(result);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void isOwnerOfTransaction_false() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        boolean result = transactionService.isOwnerOfTransaction(2L, 1L);
        assertFalse(result);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void isOwnerOfTransaction_notFound_throwsRNFException() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.isOwnerOfTransaction(1L, 1L));
        verify(transactionRepository, times(1)).findById(1L);
    }
}
