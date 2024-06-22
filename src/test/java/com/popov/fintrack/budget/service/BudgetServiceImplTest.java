package com.popov.fintrack.budget.service;

import com.popov.fintrack.budget.BudgetRepository;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.report.service.ExpenseService;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
class BudgetServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private WalletService walletService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget budget;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setId(1L);

        budget = new Budget();
        budget.setId(1L);
        budget.setOwner(userService.getUserById(1L));
        budget.setBudgetedAmount(1000.0);
        budget.setStartDate(LocalDate.now().minusDays(10));
        budget.setEndDate(LocalDate.now().plusDays(10));
        budget.setWallets(List.of(wallet));
    }

    @Test
    void getBudgetById_success() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(expenseService.getTotalFilteredExpenses(any(FilterDTO.class))).thenReturn(500.0);

        Budget foundBudget = budgetService.getBudgetById(1L);

        assertNotNull(foundBudget);
        assertEquals(1L, foundBudget.getId());
        assertEquals(500.0, foundBudget.getSpentAmount());
        assertEquals(500.0, foundBudget.getRemainingAmount());
        verify(budgetRepository, times(1)).findById(1L);
    }

    @Test
    void getBudgetById_notFound() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> budgetService.getBudgetById(1L));
        verify(budgetRepository, times(1)).findById(1L);
    }

//    @Test
//    void getBudgets_success() {
//        when(budgetRepository.findByOwnerId(1L)).thenReturn(List.of(budget));
//        when(walletService.getMemberWallets(1L)).thenReturn(List.of(wallet));
//        when(expenseService.getTotalFilteredExpenses(any(FilterDTO.class))).thenReturn(500.0);
//
//        List<Budget> budgets = budgetService.getBudgets(1L);
//
//        assertNotNull(budgets);
//        assertFalse(budgets.isEmpty());
//        verify(budgetRepository, times(1)).findByOwnerId(1L);
//        verify(walletService, times(1)).getMemberWallets(1L);
//    }

    @Test
    void updateBudget_success() {
        when(userService.getUserById(anyLong())).thenReturn(budget.getOwner());
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getAuthenticatedUserId).thenReturn(1L);

            Budget updatedBudget = budgetService.updateBudget(budget);

            assertNotNull(updatedBudget);
            assertEquals(1L, updatedBudget.getId());
            verify(budgetRepository, times(1)).save(any(Budget.class));
        }
    }

    @Test
    void deleteBudget_success() {
        doNothing().when(budgetRepository).deleteById(1L);

        budgetService.deleteBudget(1L);

        verify(budgetRepository, times(1)).deleteById(1L);
    }

    @Test
    void isOwnerOfBudget_true() {
        when(budgetRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);

        boolean result = budgetService.isOwnerOfBudget(1L, 1L);

        assertTrue(result);
        verify(budgetRepository, times(1)).existsByIdAndOwnerId(1L, 1L);
    }

    @Test
    void isOwnerOfBudget_false() {
        when(budgetRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(false);

        boolean result = budgetService.isOwnerOfBudget(1L, 1L);

        assertFalse(result);
        verify(budgetRepository, times(1)).existsByIdAndOwnerId(1L, 1L);
    }
}
