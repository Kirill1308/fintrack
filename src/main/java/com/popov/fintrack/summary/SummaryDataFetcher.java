package com.popov.fintrack.summary;

import com.popov.fintrack.report.service.ExpenseService;
import com.popov.fintrack.report.service.IncomeService;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.FinancialSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryDataFetcher {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public FinancialSummary fetchFinancialSummary(FilterDTO filters) {
        log.info("Fetching financial summary with filters: {}", filters);
        Double currentBalance = getWalletsBalance(filters.getWalletIds());
        Double totalExpenses = expenseService.getTotalFilteredExpenses(filters);
        Double totalIncome = incomeService.getTotalFilteredIncome(filters);

        return FinancialSummary.builder()
                .currentBalance(currentBalance)
                .totalChange(totalIncome - totalExpenses)
                .totalExpenses(totalExpenses)
                .totalIncome(totalIncome)
                .build();
    }

    public CustomSummary fetchCustomData(FilterDTO filters) {
        log.info("Fetching custom data with filters: {}", filters);
        Double totalExpenses = expenseService.getTotalFilteredExpenses(filters);
        Double totalIncome = incomeService.getTotalFilteredIncome(filters);
        Double averageDailyExpense = expenseService.getAverageDailyExpense(filters);
        Double averageDailyIncome = incomeService.getAverageDailyIncome(filters);

        Map<LocalDate, Double> expensesPerPeriod = expenseService.getExpensesPerDate(filters);
        Map<LocalDate, Double> incomesPerPeriod = incomeService.getIncomesPerDate(filters);

        Map<Category, Double> expensesPerCategory = expenseService.getExpensesPerCategory(filters);
        Map<Category, Double> incomePerCategory = incomeService.getIncomePerCategory(filters);

        return CustomSummary.builder()
                .walletId(filters.getWalletIds().get(0))
                .startDate(filters.getDateRange().getStartDate())
                .endDate(filters.getDateRange().getEndDate())
                .totalExpenses(totalExpenses)
                .totalIncome(totalIncome)
                .averageDailyExpense(averageDailyExpense)
                .averageDailyIncome(averageDailyIncome)
                .expensesPerPeriod(expensesPerPeriod)
                .incomesPerPeriod(incomesPerPeriod)
                .expensesPerCategory(expensesPerCategory)
                .incomePerCategory(incomePerCategory)
                .build();
    }

    public YearlySummary fetchYearlyData(int year, Long walletId) {
        log.info("Fetching yearly data for year: {} and wallet ID: {}", year, walletId);
        Double totalExpenses = expenseService.getTotalExpensesForYear(year, walletId);
        Month mostExpensiveMonth = expenseService.getMostExpensiveMonth(year, walletId);
        Month leastExpensiveMonth = expenseService.getLeastExpensiveMonth(year, walletId);
        Double averageMonthlyExpense = expenseService.getAverageMonthlyExpense(year, walletId);

        Double totalIncome = incomeService.getTotalIncomeForYear(year, walletId);
        Month highestIncomeMonth = incomeService.getHighestIncomeMonth(year, walletId);
        Month lowestIncomeMonth = incomeService.getLowestIncomeMonth(year, walletId);
        Double averageMonthlyIncome = incomeService.getAverageMonthlyIncome(year, walletId);

        Map<Month, Double> expensesPerMonth = expenseService.getExpensesPerMonth(year, walletId);
        Map<Month, Double> incomesPerMonth = incomeService.getIncomesPerMonth(year, walletId);

        Map<Category, Double> expensesPerCategory = expenseService.getExpensesPerCategory(year, walletId);
        Map<Category, Double> incomePerCategory = incomeService.getIncomePerCategory(year, walletId);

        return YearlySummary.builder()
                .year(year)
                .walletId(walletId)
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

    public MonthlySummary fetchMonthlyData(int year, Month month, Long walletId) {
        log.info("Fetching monthly data for year: {}, month: {} and wallet ID: {}", year, month, walletId);
        Double totalExpenses = expenseService.getTotalExpensesForMonth(year, month, walletId);
        Double totalIncome = incomeService.getTotalIncomeForMonth(year, month, walletId);
        Double averageDailyExpense = expenseService.getAverageDailyExpense(year, month, walletId);
        Double averageDailyIncome = incomeService.getAverageDailyIncome(year, month, walletId);
        Double averageDailyBalance = averageDailyIncome - averageDailyExpense;
        Double netSavings = totalIncome - totalExpenses;
        Double highestDailyExpense = expenseService.getHighestDailyExpense(year, month, walletId);
        Double lowestDailyIncome = incomeService.getLowestDailyIncome(year, month, walletId);

        return MonthlySummary.builder()
                .year(year)
                .month(month)
                .walletId(walletId)
                .totalExpenses(totalExpenses)
                .totalIncome(totalIncome)
                .averageDailyExpense(averageDailyExpense)
                .averageDailyIncome(averageDailyIncome)
                .averageDailyBalance(averageDailyBalance)
                .netSavings(netSavings)
                .highestDailyExpense(highestDailyExpense)
                .lowestDailyIncome(lowestDailyIncome)
                .build();
    }

    private Double getWalletsBalance(List<Long> walletIds) {
        log.info("Fetching wallets balance for wallet IDs: {}", walletIds);
        return walletIds.stream()
                .mapToDouble(walletId -> expenseService.getTotalExpensesForYear(LocalDate.now().getYear(), walletId) -
                                         incomeService.getTotalIncomeForYear(LocalDate.now().getYear(), walletId))
                .sum();
    }
}
