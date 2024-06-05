package com.popov.fintrack.budget.service;

import com.popov.fintrack.budget.BudgetRepository;
import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.budget.model.BudgetStatus;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.report.service.impl.ExpenseServiceImpl;
import com.popov.fintrack.transaction.dto.DateRange;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.wallet.InvitationService;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Invitation;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final UserService userService;
    private final WalletService walletService;
    private final BudgetRepository budgetRepository;
    private final ExpenseServiceImpl expenseService;

    private final InvitationService invitationService;

    @Override
    @Transactional(readOnly = true)
    public Budget getBudgetById(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));

        updateBudgetAmounts(budget);
        return budget;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> getBudgetsByUserId(Long userId) {
        List<Budget> budgets = budgetRepository.findByUserId(userId);
        budgets.forEach(this::updateBudgetAmounts);
        return budgets;
    }

    @Override
    @Transactional
    public Budget createBudget(Budget budget) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        budget.setUser(userService.getUserById(userId));
        budget.setWallet(walletService.getWalletById(budget.getWallet().getId()));
        budget.setStatus(BudgetStatus.ACTIVE);
        return budgetRepository.save(budget);
    }

    @Override
    @Transactional
    public Budget updateBudget(Budget budget) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        budget.setUser(userService.getUserById(userId));
        return budgetRepository.save(budget);
    }

    @Override
    @Transactional
    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfBudget(Long userId, Long budgetId) {
        return budgetRepository.existsByUserIdAndId(userId, budgetId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> getInvitedBudgets(Long userId) {
        List<Invitation> invitations = invitationService.getAcceptedInvitations(userId);

        return invitations.stream()
                .map(Invitation::getWallet)
                .flatMap(wallet -> wallet.getBudgets().stream())
                .peek(this::updateBudgetAmounts)
                .toList();
    }

    private void updateBudgetAmounts(Budget budget) {
        FilterDTO filters = createFilterFromBudget(budget);
        List<Long> walletIds = walletService.getWallets(SecurityUtils.getAuthenticatedUserId())
                .stream().map(Wallet::getId).toList();
        filters.setWalletIds(walletIds);

        Double totalAmountSpent = expenseService.getTotalFilteredExpenses(filters);
        double remainingAmount = budget.getBudgetedAmount() - totalAmountSpent;
        long daysBetween = ChronoUnit.DAYS.between(budget.getStartDate(), budget.getEndDate());
        Double availableAmountPerDay = remainingAmount / daysBetween;

        budget.setSpentAmount(totalAmountSpent);
        budget.setRemainingAmount(remainingAmount);
        budget.setAvailableAmountPerDay(availableAmountPerDay);

        if (remainingAmount < 0) {
            budget.setStatus(BudgetStatus.OVERSPENT);
        } else if (totalAmountSpent >= budget.getBudgetedAmount()) {
            budget.setStatus(BudgetStatus.COMPLETED);
        }
    }

    private FilterDTO createFilterFromBudget(Budget budget) {
        FilterDTO filters = new FilterDTO();
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(budget.getStartDate());
        dateRange.setEndDate(budget.getEndDate());

        filters.setDateRange(dateRange);
        filters.setCategories(List.of(budget.getCategory()));
        return filters;
    }
}
