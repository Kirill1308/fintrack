package com.popov.fintrack.budget;

import com.popov.fintrack.budget.model.Budget;

import java.util.List;

public interface BudgetService {

    Budget getBudgetById(Long budgetId);

    List<Budget> getBudgetsByUserId(Long userId);

    boolean isMemberOfBudget(Long userId, Long budgetId);

    Budget createBudget(Budget budget);

    Budget updateBudget(Budget budget);

    void deleteBudget(Long budgetId);
}
