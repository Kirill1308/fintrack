package com.popov.fintrack.report.service.impl;

import com.popov.fintrack.report.repository.IncomeRepository;
import com.popov.fintrack.report.service.IncomeService;
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
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;

    @Override
    public Double getTotalFilteredIncome(FilterDTO filters) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, Type.INCOME);
        return incomeRepository.findAll(spec).stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public Double getTotalIncomeForYear(int year, Long walletId) {
        Double total = incomeRepository.getTotalIncomeForYear(year, walletId);
        return total == null ? 0.0 : total;
    }

    @Override
    public Month getHighestIncomeMonth(int year, Long walletId) {
        List<Object[]> results = incomeRepository.getHighestIncomeMonth(year, walletId);
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            int monthNumber = ((Number) result[0]).intValue();
            return Month.of(monthNumber);
        }
        return null;
    }

    @Override
    public Month getLowestIncomeMonth(int year, Long walletId) {
        List<Object[]> results = incomeRepository.getLowestIncomeMonth(year, walletId);
        if (!results.isEmpty()) {
            Object[] result = results.get(0);
            int monthNumber = ((Number) result[0]).intValue();
            return Month.of(monthNumber);
        }
        return null;
    }

    @Override
    public Double getAverageMonthlyIncome(int year, Long walletId) {
        Double average = incomeRepository.getAverageMonthlyIncome(year, walletId);
        return average == null ? 0.0 : average;
    }

    @Override
    public Map<Month, Double> getIncomesPerMonth(int year, Long walletId) {
        List<Object[]> results = incomeRepository.getIncomesPerMonth(year, walletId);

        Map<Month, Double> incomesPerMonth = new EnumMap<>(Month.class);

        for (Object[] result : results) {
            int monthNumber = ((Number) result[0]).intValue();
            Month month = Month.of(monthNumber);
            double total = ((Number) result[1]).doubleValue();
            incomesPerMonth.put(month, total);
        }

        return incomesPerMonth;
    }

    @Override
    public Map<Category, Double> getIncomePerCategory(int year, Long walletId) {
        List<Object[]> results = incomeRepository.getIncomePerCategory(year, walletId);
        Map<Category, Double> incomePerCategory = new EnumMap<>(Category.class);

        for (Object[] result : results) {
            Category category = (Category) result[0];
            double total = ((Number) result[1]).doubleValue();
            incomePerCategory.put(category, total);
        }

        return incomePerCategory;
    }

    @Override
    public Double getTotalIncomeForMonth(int year, Month month, Long walletId) {
        Double total = incomeRepository.getTotalIncomeForMonth(year, month.getValue(), walletId);
        return total == null ? 0.0 : total;
    }

    @Override
    public Double getAverageDailyIncome(int year, Month month, Long walletId) {
        Double total = incomeRepository.getTotalIncomeForMonth(year, month.getValue(), walletId);
        if (total == null) {
            return 0.0;
        }

        int daysInMonth = month.length(false);
        return total / daysInMonth;
    }

    @Override
    public Double getLowestDailyIncome(int year, Month month, Long walletId) {
        Double total = incomeRepository.getLowestDailyIncome(year, month.getValue(), walletId);
        return total == null ? 0.0 : total;
    }

    @Override
    public Double getAverageDailyIncome(FilterDTO filters) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, Type.INCOME);
        List<Transaction> transactions = incomeRepository.findAll(spec);

        if (transactions.isEmpty()) {
            return 0.0;
        }

        double total = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        int days = transactions.size();
        return total / days;
    }

    @Override
    public Map<LocalDate, Double> getIncomesPerDate(FilterDTO filters) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, Type.INCOME);
        List<Transaction> transactions = incomeRepository.findAll(spec);

        Map<LocalDate, Double> incomesPerPeriod = new HashMap<>();

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDateCreated();
            double total = transaction.getAmount();
            incomesPerPeriod.put(date, total);
        }

        return incomesPerPeriod;
    }

    @Override
    public Map<Category, Double> getIncomePerCategory(FilterDTO filters) {
        Specification<Transaction> spec = SpecificationUtils.buildSpecification(filters, Type.INCOME);
        List<Object[]> results = incomeRepository.getIncomePerCategory(spec);

        Map<Category, Double> incomePerCategory = new EnumMap<>(Category.class);

        for (Object[] result : results) {
            Category category = (Category) result[0];
            double total = ((Number) result[1]).doubleValue();
            incomePerCategory.put(category, total);
        }

        return incomePerCategory;
    }
}
