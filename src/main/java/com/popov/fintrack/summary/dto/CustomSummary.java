package com.popov.fintrack.summary.dto;

import com.popov.fintrack.transaction.model.Category;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class CustomSummary {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Double totalExpenses;
    private final Double totalIncome;
    private final Double averageDailyExpense;
    private final Double averageDailyIncome;

    private final Map<LocalDate, Double> expensesPerPeriod;
    private final Map<LocalDate, Double> incomesPerPeriod;

    private final Map<Category, Double> expensesPerCategory;
    private final Map<Category, Double> incomePerCategory;
}
