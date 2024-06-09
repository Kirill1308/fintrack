package com.popov.fintrack.summary.dto;

import com.popov.fintrack.transaction.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Month;
import java.util.Map;

@Getter
@Builder
@Schema(description = "Summary of yearly financial data including expenses, income, and various monthly breakdowns.")
public class YearlySummary {

    @Schema(description = "Year of the summary", example = "2023")
    private int year;

    @Schema(description = "ID of the wallet associated with this summary", example = "123")
    private long walletId;

    @Schema(description = "Total expenses for the year", example = "18000.50")
    private final Double totalExpenses;

    @Schema(description = "Month with the highest expenses", example = "JULY")
    private final Month mostExpensiveMonth;

    @Schema(description = "Month with the lowest expenses", example = "FEBRUARY")
    private final Month leastExpensiveMonth;

    @Schema(description = "Average monthly expenses for the year", example = "1500.00")
    private final Double averageMonthlyExpense;

    @Schema(description = "Total income for the year", example = "20000.00")
    private final Double totalIncome;

    @Schema(description = "Month with the highest income", example = "DECEMBER")
    private final Month highestIncomeMonth;

    @Schema(description = "Month with the lowest income", example = "JANUARY")
    private final Month lowestIncomeMonth;

    @Schema(description = "Average monthly income for the year", example = "1666.67")
    private final Double averageMonthlyIncome;

    @Schema(description = "Map of expenses per month")
    private final Map<Month, Double> expensesPerMonth;

    @Schema(description = "Map of incomes per month")
    private final Map<Month, Double> incomesPerMonth;

    @Schema(description = "Map of expenses per category")
    private final Map<Category, Double> expensesPerCategory;

    @Schema(description = "Map of incomes per category")
    private final Map<Category, Double> incomePerCategory;
}
