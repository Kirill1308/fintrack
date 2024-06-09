package com.popov.fintrack.summary.dto;

import com.popov.fintrack.transaction.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
@Schema(description = "Custom summary of financial data for a specified period.")
public class CustomSummary {

    @Schema(description = "ID of the wallet", example = "1")
    private final Long walletId;

    @Schema(description = "Start date of the custom summary period", example = "2023-01-01")
    private final LocalDate startDate;

    @Schema(description = "End date of the custom summary period", example = "2023-12-31")
    private final LocalDate endDate;

    @Schema(description = "Total expenses during the period", example = "1500.50")
    private final Double totalExpenses;

    @Schema(description = "Total income during the period", example = "2000.75")
    private final Double totalIncome;

    @Schema(description = "Average daily expense during the period", example = "50.02")
    private final Double averageDailyExpense;

    @Schema(description = "Average daily income during the period", example = "66.69")
    private final Double averageDailyIncome;

    @Schema(description = "Map of expenses per day during the period")
    private final Map<LocalDate, Double> expensesPerPeriod;

    @Schema(description = "Map of incomes per day during the period")
    private final Map<LocalDate, Double> incomesPerPeriod;

    @Schema(description = "Map of expenses per category during the period")
    private final Map<Category, Double> expensesPerCategory;

    @Schema(description = "Map of incomes per category during the period")
    private final Map<Category, Double> incomePerCategory;
}
