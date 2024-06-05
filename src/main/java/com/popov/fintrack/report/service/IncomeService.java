package com.popov.fintrack.report.service;

import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;

public interface IncomeService {

    Double getTotalFilteredIncome(FilterDTO filters);

    Double getTotalIncomeForYear(int year, Long walletId);

    Double getTotalIncomeForMonth(int year, Month month, Long walletId);

    Month getHighestIncomeMonth(int year, Long walletId);

    Month getLowestIncomeMonth(int year, Long walletId);

    Double getAverageMonthlyIncome(int year, Long walletId);

    Double getAverageDailyIncome(int year, Month month, Long walletId);

    Double getAverageDailyIncome(FilterDTO filters);

    Map<Month, Double> getIncomesPerMonth(int year, Long walletId);

    Map<LocalDate, Double> getIncomesPerDate(FilterDTO filters);

    Map<Category, Double> getIncomePerCategory(int year, Long walletId);

    Map<Category, Double> getIncomePerCategory(FilterDTO filters);

    Double getLowestDailyIncome(int year, Month month, Long walletId);
}