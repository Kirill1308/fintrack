package com.popov.fintrack.report.data;

import com.popov.fintrack.report.service.ExpenseService;
import com.popov.fintrack.report.service.IncomeService;
import com.popov.fintrack.transaction.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReportDataFetcher {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @Transactional(readOnly = true)
    public YearlySummary fetchYearlyData(int year, Long userId) {
        Double totalExpenses = expenseService.getTotalExpensesForYear(year, userId);
        Month mostExpensiveMonth = expenseService.getMostExpensiveMonth(year, userId);
        Month leastExpensiveMonth = expenseService.getLeastExpensiveMonth(year, userId);
        Double averageMonthlyExpense = expenseService.getAverageMonthlyExpense(year, userId);

        Double totalIncome = incomeService.getTotalIncomeForYear(year, userId);
        Month highestIncomeMonth = incomeService.getHighestIncomeMonth(year, userId);
        Month lowestIncomeMonth = incomeService.getLowestIncomeMonth(year, userId);
        Double averageMonthlyIncome = incomeService.getAverageMonthlyIncome(year, userId);

        Map<Month, Double> expensesPerMonth = expenseService.getExpensesPerMonth(year, userId);
        Map<Month, Double> incomesPerMonth = incomeService.getIncomesPerMonth(year, userId);

        Map<Category, Double> expensesPerCategory = expenseService.getExpensesPerCategory(year, userId);
        Map<Category, Double> incomePerCategory = incomeService.getIncomePerCategory(year, userId);

        return YearlySummary.builder()
                .year(year)
                .totalExpenses(totalExpenses)
                .mostExpensiveMonth(mostExpensiveMonth)
                .leastExpensiveMonth(leastExpensiveMonth)
                .averageMonthlyExpense(averageMonthlyExpense)
                .totalIncome(totalIncome)
                .highestIncomeMonth(highestIncomeMonth)
                .lowestIncomeMonth(lowestIncomeMonth)
                .averageMonthlyIncome(averageMonthlyIncome)
                .expensesPerMonth(expensesPerMonth)
                .incomesPerMonth(incomesPerMonth)
                .expensesPerCategory(expensesPerCategory)
                .incomePerCategory(incomePerCategory)
                .build();
    }

    public MonthlySummary fetchMonthlyData(int year, Month month, Long userId) {
        Double totalExpenses = expenseService.getTotalExpensesForMonth(year, month, userId);
        Double totalIncome = incomeService.getTotalIncomeForMonth(year, month, userId);
        Double averageDailyExpense = expenseService.getAverageDailyExpense(year, month, userId);
        Double averageDailyIncome = incomeService.getAverageDailyIncome(year, month, userId);
        Double averageDailyBalance = averageDailyIncome - averageDailyExpense;

        return MonthlySummary.builder()
                .year(year)
                .month(month)
                .totalExpenses(totalExpenses)
                .totalIncome(totalIncome)
                .averageDailyExpense(averageDailyExpense)
                .averageDailyIncome(averageDailyIncome)
                .averageDailyBalance(averageDailyBalance)
                .build();
    }
}
