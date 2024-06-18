package com.popov.fintrack.report;

import com.popov.fintrack.report.dto.CustomReportRequest;
import com.popov.fintrack.report.dto.ReportRequest;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.DateRange;

import java.time.LocalDate;
import java.time.Month;

public class ReportTestData {

    public static final ReportRequest reportRequest;
    public static final CustomReportRequest customReportRequest;
    public static final YearlySummary yearlySummary;
    public static final MonthlySummary monthlySummary;
    public static final CustomSummary customSummary;

    public static final DateRange dateRange;

    static {
        reportRequest = ReportRequest.builder()
                .year(2024)
                .month(Month.JANUARY)
                .walletId(1L)
                .format("pdf")
                .build();

        dateRange = DateRange.builder()
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .build();

        customReportRequest = CustomReportRequest.builder()
                .walletId(1L)
                .format("pdf")
                .dateRange(dateRange)
                .build();

        yearlySummary = YearlySummary.builder()
                .year(2024)
                .walletId(1L)
                .totalExpenses(500.0)
                .mostExpensiveMonth(Month.JANUARY)
                .leastExpensiveMonth(Month.FEBRUARY)
                .averageMonthlyExpense(200.0)
                .totalIncome(1000.0)
                .highestIncomeMonth(Month.MARCH)
                .lowestIncomeMonth(Month.APRIL)
                .averageMonthlyIncome(300.0)
                .build();

        monthlySummary = MonthlySummary.builder()
                .year(2024)
                .month(Month.JANUARY)
                .walletId(1L)
                .totalExpenses(500.0)
                .totalIncome(1000.0)
                .averageDailyExpense(20.0)
                .averageDailyIncome(40.0)
                .averageDailyBalance(20.0)
                .netSavings(500.0)
                .highestDailyExpense(50.0)
                .lowestDailyIncome(10.0)
                .build();

        customSummary = CustomSummary.builder()
                .walletId(1L)
                .startDate(dateRange.getStartDate())
                .endDate(dateRange.getEndDate())
                .totalExpenses(500.0)
                .totalIncome(1000.0)
                .averageDailyExpense(20.0)
                .averageDailyIncome(40.0)
                .build();
    }
}
