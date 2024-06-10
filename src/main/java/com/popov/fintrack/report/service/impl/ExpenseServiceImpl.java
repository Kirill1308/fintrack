package com.popov.fintrack.report.service.impl;

import com.popov.fintrack.report.repository.ExpenseRepository;
import com.popov.fintrack.report.service.ExpenseService;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.transaction.model.Transaction;
import com.popov.fintrack.transaction.model.Type;
import com.popov.fintrack.utills.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Double getTotalFilteredExpenses(FilterDTO filterDTO) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filterDTO, Type.EXPENSE);
        return expenseRepository.findAll(spec).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public Double getTotalExpensesForYear(int year, Long walletId) {
        Double total = expenseRepository.getTotalExpensesForYear(year, walletId);
        return total == null ? Double.valueOf(0.0) : total;
    }

    @Override
    public Month getMostExpensiveMonth(int year, Long walletId) {
        List<Object[]> results = expenseRepository.getMostExpensiveMonth(year, walletId);
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            int monthNumber = ((Number) result[0]).intValue();
            return Month.of(monthNumber);
        }
        return null;
    }

    @Override
    public Month getLeastExpensiveMonth(int year, Long walletId) {
        List<Object[]> results = expenseRepository.getLeastExpensiveMonth(year, walletId);
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            int monthNumber = ((Number) result[0]).intValue();
            return Month.of(monthNumber);
        }
        return null;
    }

    @Override
    public Double getAverageMonthlyExpense(int year, Long walletId) {
        Double average = expenseRepository.getAverageMonthlyExpense(year, walletId);
        return average == null ? Double.valueOf(0.0) : average;
    }

    @Override
    public Map<Month, Double> getExpensesPerMonth(int year, Long walletId) {
        List<Object[]> results = expenseRepository.getExpensesPerMonth(year, walletId);

        Map<Month, Double> expensesPerMonth = new EnumMap<>(Month.class);

        for (Object[] result : results) {
            int monthNumber = ((Number) result[0]).intValue();
            Month month = Month.of(monthNumber);
            double total = ((Number) result[1]).doubleValue();
            expensesPerMonth.put(month, total);
        }

        return expensesPerMonth;
    }

    @Override
    public Map<Category, Double> getExpensesPerCategory(int year, Long walletId) {
        List<Object[]> results = expenseRepository.getExpensesPerCategory(year, walletId);

        Map<Category, Double> expensesPerCategory = new EnumMap<>(Category.class);

        for (Object[] result : results) {
            if (result != null) {
                Category category = (Category) result[0];
                double total = ((Number) result[1]).doubleValue();
                expensesPerCategory.put(category, total);
            }
        }

        return expensesPerCategory;
    }

    @Override
    public Double getTotalExpensesForMonth(int year, Month month, Long walletId) {
        Double total = expenseRepository.getTotalExpensesForMonth(year, month.getValue(), walletId);
        return total == null ? 0.0 : total;
    }

    @Override
    public Double getAverageDailyExpense(int year, Month month, Long walletId) {
        Double average = expenseRepository.getAverageDailyExpense(year, month.getValue(), walletId);
        return average == null ? 0.0 : average;
    }

    @Override
    public Double getHighestDailyExpense(int year, Month month, Long walletId) {
        Double highest = expenseRepository.getHighestDailyExpense(year, month.getValue(), walletId);
        return highest == null ? 0.0 : highest;
    }

    @Override
    public Double getAverageDailyExpense(FilterDTO filterDTO) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filterDTO, Type.EXPENSE);
        Double average = expenseRepository.getAverageDailyExpense(spec);
        return average == null ? 0.0 : average;
    }

    @Override
    public Map<LocalDate, Double> getExpensesPerDate(FilterDTO filters) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, Type.EXPENSE);
        List<Transaction> transactions = expenseRepository.findAll(spec);

        Map<LocalDate, Double> expensesPerPeriod = new HashMap<>();

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDateCreated();
            double amount = transaction.getAmount();
            expensesPerPeriod.put(date, amount);
        }

        return expensesPerPeriod;
    }

    @Override
    public Map<Category, Double> getExpensesPerCategory(FilterDTO filters) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, Type.EXPENSE);
        List<Transaction> transactions = expenseRepository.findAll(spec);

        Map<Category, Double> expensesPerCategory = new EnumMap<>(Category.class);

        for (Transaction transaction : transactions) {
            Category category = transaction.getCategory();
            double amount = transaction.getAmount();
            expensesPerCategory.put(category, expensesPerCategory.getOrDefault(category, 0.0) + amount);
        }

        return expensesPerCategory;
    }
}
