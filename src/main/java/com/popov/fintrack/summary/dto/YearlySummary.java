package com.popov.fintrack.summary.dto;

import com.popov.fintrack.transaction.model.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.Month;
import java.util.Map;

@Getter
@Builder
public class YearlySummary {
    private int year;
    private long walletId;
    private final Double totalExpenses;
    private final Month mostExpensiveMonth;
    private final Month leastExpensiveMonth;
    private final Double averageMonthlyExpense;

    private final Double totalIncome;
    private final Month highestIncomeMonth;
    private final Month lowestIncomeMonth;
    private final Double averageMonthlyIncome;

    private final Map<Month, Double> expensesPerMonth;
    private final Map<Month, Double> incomesPerMonth;

    private final Map<Category, Double> expensesPerCategory;
    private final Map<Category, Double> incomePerCategory;
}
