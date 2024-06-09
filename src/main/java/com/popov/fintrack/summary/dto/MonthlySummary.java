package com.popov.fintrack.summary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Month;

@Data
@Builder
@Schema(description = "Summary of monthly financial data including expenses, income, and various averages.")
public class MonthlySummary {

    @Schema(description = "Year of the summary", example = "2023")
    private final int year;

    @Schema(description = "Month of the summary", example = "JANUARY")
    private final Month month;

    @Schema(description = "ID of the wallet associated with this summary", example = "123")
    private final long walletId;

    @Schema(description = "Total expenses for the month", example = "1500.50")
    private final Double totalExpenses;

    @Schema(description = "Total income for the month", example = "1300.50")
    private final Double totalIncome;

    @Schema(description = "Average daily expense for the month", example = "50.00")
    private final Double averageDailyExpense;

    @Schema(description = "Average daily income for the month", example = "43.33")
    private final Double averageDailyIncome;

    @Schema(description = "Average daily balance for the month", example = "3000.00")
    private final Double averageDailyBalance;

    @Schema(description = "Net savings for the month", example = "200.00")
    private final Double netSavings;

    @Schema(description = "Highest daily expense recorded in the month", example = "200.00")
    private final Double highestDailyExpense;

    @Schema(description = "Lowest daily income recorded in the month", example = "20.00")
    private final Double lowestDailyIncome;
}
