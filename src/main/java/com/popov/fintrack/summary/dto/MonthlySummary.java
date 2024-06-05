package com.popov.fintrack.summary.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Month;

@Data
@Builder
public class MonthlySummary {
    private final int year;
    private final Month month;
    private final Double totalExpenses;
    private final Double totalIncome;
    private final Double averageDailyExpense;
    private final Double averageDailyIncome;
    private final Double averageDailyBalance;
    private final Double netSavings;
    private final Double highestDailyExpense;
    private final Double lowestDailyIncome;
}
