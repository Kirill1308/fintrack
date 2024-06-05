package com.popov.fintrack.summary.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancialSummary {
    private Double currentBalance;
    private Double totalChange;
    private Double totalExpenses;
    private Double totalIncome;
}
