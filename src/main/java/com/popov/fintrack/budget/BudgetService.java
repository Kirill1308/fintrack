package com.popov.fintrack.budget;

import com.popov.fintrack.budget.model.Budget;

import java.util.List;

public interface BudgetService {

    Budget getBudgetById(Long budgetId);

    List<Budget> getBudgets(Long userId);

    List<Budget> getOwnedBudgets(Long userId);

    List<Budget> getMemberBudgets(Long userId);

    boolean isOwnerOfBudget(Long userId, Long budgetId);

    Budget updateBudget(Budget budget);

    void deleteBudget(Long budgetId);
}
