package com.popov.fintrack.budget.service;

import com.popov.fintrack.budget.BudgetRepository;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.report.service.ExpenseService;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.popov.fintrack.budget.BudgetTestData.OWNED_BUDGET_ID;
import static com.popov.fintrack.budget.BudgetTestData.budget;
import static com.popov.fintrack.user.UserTestData.USER1_ID;
import static com.popov.fintrack.user.UserTestData.user;
import static com.popov.fintrack.wallet.WalletTestData.wallet;
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

    @Test
    void getBudgetById_success() {
        when(budgetRepository.findById(OWNED_BUDGET_ID)).thenReturn(Optional.of(budget));
        when(expenseService.getTotalFilteredExpenses(any(FilterDTO.class))).thenReturn(500.0);

        Budget foundBudget = budgetService.getBudgetById(OWNED_BUDGET_ID);

        assertNotNull(foundBudget);
        assertEquals(budget.getId(), foundBudget.getId());
    }

    @Test
    void getBudgetById_notFound() {
        when(budgetRepository.findById(OWNED_BUDGET_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> budgetService.getBudgetById(OWNED_BUDGET_ID));
    }

    @Test
    void getBudgets_success() {
        when(budgetRepository.findByOwnerId(user.getId())).thenReturn(List.of(budget));
        when(walletService.getMemberWallets(user.getId())).thenReturn(List.of(wallet));
        when(expenseService.getTotalFilteredExpenses(any(FilterDTO.class))).thenReturn(500.0);

        List<Budget> budgets = budgetService.getBudgets(user.getId());

        assertNotNull(budgets);
        assertFalse(budgets.isEmpty());
    }

    @Test
    void updateBudget_success() {
        when(userService.getUserById(anyLong())).thenReturn(budget.getOwner());
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getAuthenticatedUserId).thenReturn(USER1_ID);

            Budget updatedBudget = budgetService.updateBudget(budget);

            assertNotNull(updatedBudget);
            assertEquals(budget.getId(), updatedBudget.getId());
        }
    }

    @Test
    void deleteBudget_success() {
        doNothing().when(budgetRepository).deleteById(OWNED_BUDGET_ID);

        budgetService.deleteBudget(OWNED_BUDGET_ID);

        verify(budgetRepository, times(1)).deleteById(OWNED_BUDGET_ID);
    }

    @Test
    void isOwnerOfBudget_true() {
        when(budgetRepository.existsByIdAndOwnerId(OWNED_BUDGET_ID, USER1_ID)).thenReturn(true);

        boolean result = budgetService.isOwnerOfBudget(USER1_ID, OWNED_BUDGET_ID);

        assertTrue(result);
    }

    @Test
    void isOwnerOfBudget_false() {
        when(budgetRepository.existsByIdAndOwnerId(OWNED_BUDGET_ID, USER1_ID)).thenReturn(false);

        boolean result = budgetService.isOwnerOfBudget(USER1_ID, OWNED_BUDGET_ID);

        assertFalse(result);
    }
}
