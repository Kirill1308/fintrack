package com.popov.fintrack.budget.service;

import com.popov.fintrack.budget.BudgetRepository;
import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.budget.model.BudgetStatus;
import com.popov.fintrack.exception.ResourceNotFoundException;
import com.popov.fintrack.report.service.ExpenseService;
import com.popov.fintrack.transaction.dto.DateRange;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.user.UserService;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final UserService userService;
    private final WalletService walletService;
    private final ExpenseService expenseService;

    private final BudgetRepository budgetRepository;

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
    public List<Budget> getBudgets(Long userId) {
        List<Budget> ownedBudgets = getOwnedBudgets(userId);
        List<Budget> memberBudgets = getMemberBudgets(userId);

        List<Budget> budgets = new ArrayList<>();
        budgets.addAll(ownedBudgets);
        budgets.addAll(memberBudgets);

        budgets.forEach(this::updateBudgetAmounts);

        return budgets;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> getOwnedBudgets(Long userId) {
        return budgetRepository.findByOwnerId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Budget> getMemberBudgets(Long userId) {
        List<Wallet> memberWallets = walletService.getMemberWallets(userId);

        return memberWallets.stream()
                .flatMap(wallet -> wallet.getBudgets().stream())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfBudget(Long userId, Long budgetId) {
        log.info("Checking if user ID: {} is owner of budget ID: {}", userId, budgetId);
        return budgetRepository.existsByIdAndOwnerId(budgetId, userId);
    }

    @Override
    @Transactional
    public Budget updateBudget(Budget budget) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        budget.setOwner(userService.getUserById(userId));

        return budgetRepository.save(budget);
    }

    @Override
    @Transactional
    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

    private void updateBudgetAmounts(Budget budget) {
        log.debug("Updating budget amounts for budget ID: {}", budget.getId());
        FilterDTO filters = createFilterFromBudget(budget);

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
        log.debug("Creating filter from budget ID: {}", budget.getId());
        FilterDTO filters = new FilterDTO();
        List<Long> walletIds = budget.getWallets().stream()
                .map(Wallet::getId)
                .toList();
        DateRange dateRange = DateRange.builder()
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .build();

        filters.setWalletIds(walletIds);
        filters.setDateRange(dateRange);
        filters.setCategories(List.of(budget.getCategory()));
        return filters;
    }
}
