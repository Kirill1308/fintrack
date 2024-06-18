package com.popov.fintrack.summary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Month;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Summary of monthly financial data including expenses, income, and various averages.")
public class MonthlySummary {

    @Schema(description = "Year of the summary", example = "2023")
    private int year;

    @Schema(description = "Month of the summary", example = "JANUARY")
    private Month month;

    @Schema(description = "ID of the wallet associated with this summary", example = "123")
    private long walletId;

    @Schema(description = "Total expenses for the month", example = "1500.50")
    private Double totalExpenses;

    @Schema(description = "Total income for the month", example = "1300.50")
    private Double totalIncome;

    @Schema(description = "Average daily expense for the month", example = "50.00")
    private Double averageDailyExpense;

    @Schema(description = "Average daily income for the month", example = "43.33")
    private Double averageDailyIncome;

    @Schema(description = "Average daily balance for the month", example = "3000.00")
    private Double averageDailyBalance;

    @Schema(description = "Net savings for the month", example = "200.00")
    private Double netSavings;

    @Schema(description = "Highest daily expense recorded in the month", example = "200.00")
    private Double highestDailyExpense;

    @Schema(description = "Lowest daily income recorded in the month", example = "20.00")
    private Double lowestDailyIncome;
}
