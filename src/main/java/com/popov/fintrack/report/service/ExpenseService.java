package com.popov.fintrack.report.service;

import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

public interface ExpenseService {

    Double getTotalFilteredExpenses(FilterDTO filterDTO);

    Double getTotalExpensesForYear(int year, Long walletId);

    Double getTotalExpensesForMonth(int year, Month month, Long walletId);

    Month getMostExpensiveMonth(int year, Long walletId);

    Month getLeastExpensiveMonth(int year, Long walletId);

    Double getHighestDailyExpense(int year, Month month, Long walletId);

    Double getAverageMonthlyExpense(int year, Long walletId);

    Double getAverageDailyExpense(int year, Month month, Long walletId);

    Double getAverageDailyExpense(FilterDTO filterDTO);

    Map<Month, Double> getExpensesPerMonth(int year, Long walletId);

    Map<LocalDate, Double> getExpensesPerDate(FilterDTO filters);

    Map<Category, Double> getExpensesPerCategory(int year, Long walletId);

    Map<Category, Double> getExpensesPerCategory(FilterDTO filterDTO);
}
