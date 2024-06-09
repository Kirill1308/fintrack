package com.popov.fintrack.summary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Summary of financial data including balance, total change, expenses, and income.")
public class FinancialSummary {

    @Schema(description = "Current balance in the account", example = "5000.00")
    private Double currentBalance;

    @Schema(description = "Total change in balance over the period", example = "-200.00")
    private Double totalChange;

    @Schema(description = "Total expenses during the period", example = "1500.50")
    private Double totalExpenses;

    @Schema(description = "Total income during the period", example = "1300.50")
    private Double totalIncome;
}
