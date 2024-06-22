package com.popov.fintrack.report.service.impl;

import com.popov.fintrack.report.repository.ExpenseRepository;
import com.popov.fintrack.transaction.dto.FilterDTO;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.transaction.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private Transaction transaction;
    private List<Transaction> transactionList;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setDateCreated(LocalDate.now());

        transactionList = List.of(transaction);
    }

    @Test
    void getTotalFilteredExpenses_success() {
        when(expenseRepository.findAll(any(Specification.class))).thenReturn(transactionList);

        FilterDTO filterDTO = new FilterDTO();
        Double totalExpenses = expenseService.getTotalFilteredExpenses(filterDTO);

        assertEquals(100.0, totalExpenses);
        verify(expenseRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getTotalExpensesForYear_success() {
        when(expenseRepository.getTotalExpensesForYear(2024, 1L)).thenReturn(1200.0);

        Double totalExpenses = expenseService.getTotalExpensesForYear(2024, 1L);

        assertEquals(1200.0, totalExpenses);
        verify(expenseRepository, times(1)).getTotalExpensesForYear(2024, 1L);
    }

    @Test
    void getMostExpensiveMonth_success() {
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{1, 500.0});

        when(expenseRepository.getMostExpensiveMonth(2024, 1L)).thenReturn(results);

        Month month = expenseService.getMostExpensiveMonth(2024, 1L);

        assertEquals(Month.JANUARY, month);
        verify(expenseRepository, times(1)).getMostExpensiveMonth(2024, 1L);
    }

    @Test
    void getLeastExpensiveMonth_success() {
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{2, 500.0});

        when(expenseRepository.getLeastExpensiveMonth(2024, 1L)).thenReturn(results);
        Month month = expenseService.getLeastExpensiveMonth(2024, 1L);

        assertEquals(Month.FEBRUARY, month);
        verify(expenseRepository, times(1)).getLeastExpensiveMonth(2024, 1L);
    }

    @Test
    void getAverageMonthlyExpense_success() {
        when(expenseRepository.getAverageMonthlyExpense(2024, 1L)).thenReturn(100.0);

        Double averageExpense = expenseService.getAverageMonthlyExpense(2024, 1L);

        assertEquals(100.0, averageExpense);
        verify(expenseRepository, times(1)).getAverageMonthlyExpense(2024, 1L);
    }

    @Test
    void getExpensesPerMonth_success() {
        when(expenseRepository.getExpensesPerMonth(2024, 1L)).thenReturn(List.of(new Object[]{1, 500.0}, new Object[]{2, 300.0}));

        Map<Month, Double> expensesPerMonth = expenseService.getExpensesPerMonth(2024, 1L);

        assertNotNull(expensesPerMonth);
        assertEquals(2, expensesPerMonth.size());
        assertEquals(500.0, expensesPerMonth.get(Month.JANUARY));
        assertEquals(300.0, expensesPerMonth.get(Month.FEBRUARY));
        verify(expenseRepository, times(1)).getExpensesPerMonth(2024, 1L);
    }

    @Test
    void getExpensesPerCategory_success() {
        when(expenseRepository.getExpensesPerCategory(2024, 1L)).thenReturn(List.of(new Object[]{Category.GROCERIES, 200.0}, new Object[]{Category.RENT, 800.0}));

        Map<Category, Double> expensesPerCategory = expenseService.getExpensesPerCategory(2024, 1L);

        assertNotNull(expensesPerCategory);
        assertEquals(2, expensesPerCategory.size());
        assertEquals(200.0, expensesPerCategory.get(Category.GROCERIES));
        assertEquals(800.0, expensesPerCategory.get(Category.RENT));
        verify(expenseRepository, times(1)).getExpensesPerCategory(2024, 1L);
    }

    @Test
    void getTotalExpensesForMonth_success() {
        when(expenseRepository.getTotalExpensesForMonth(2024, 1, 1L)).thenReturn(100.0);

        Double totalExpenses = expenseService.getTotalExpensesForMonth(2024, Month.JANUARY, 1L);

        assertEquals(100.0, totalExpenses);
        verify(expenseRepository, times(1)).getTotalExpensesForMonth(2024, 1, 1L);
    }

    @Test
    void getAverageDailyExpense_success() {
        when(expenseRepository.getAverageDailyExpense(2024, 1, 1L)).thenReturn(10.0);

        Double averageDailyExpense = expenseService.getAverageDailyExpense(2024, Month.JANUARY, 1L);

        assertEquals(10.0, averageDailyExpense);
        verify(expenseRepository, times(1)).getAverageDailyExpense(2024, 1, 1L);
    }

    @Test
    void getHighestDailyExpense_success() {
        when(expenseRepository.getHighestDailyExpense(2024, 1, 1L)).thenReturn(50.0);

        Double highestDailyExpense = expenseService.getHighestDailyExpense(2024, Month.JANUARY, 1L);

        assertEquals(50.0, highestDailyExpense);
        verify(expenseRepository, times(1)).getHighestDailyExpense(2024, 1, 1L);
    }

    @Test
    void getAverageDailyExpense_FilterDTO_success() {
        when(expenseRepository.getAverageDailyExpense(any(Specification.class))).thenReturn(10.0);

        FilterDTO filterDTO = new FilterDTO();
        Double averageDailyExpense = expenseService.getAverageDailyExpense(filterDTO);

        assertEquals(10.0, averageDailyExpense);
        verify(expenseRepository, times(1)).getAverageDailyExpense(any(Specification.class));
    }

    @Test
    void getExpensesPerDate_success() {
        when(expenseRepository.findAll(any(Specification.class))).thenReturn(transactionList);

        FilterDTO filterDTO = new FilterDTO();
        Map<LocalDate, Double> expensesPerDate = expenseService.getExpensesPerDate(filterDTO);

        assertNotNull(expensesPerDate);
        assertEquals(1, expensesPerDate.size());
        assertEquals(100.0, expensesPerDate.get(transaction.getDateCreated()));
        verify(expenseRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getExpensesPerCategory_FilterDTO_success() {
        when(expenseRepository.findAll(any(Specification.class))).thenReturn(transactionList);

        FilterDTO filterDTO = new FilterDTO();
        transaction.setCategory(Category.GROCERIES);
        Map<Category, Double> expensesPerCategory = expenseService.getExpensesPerCategory(filterDTO);

        assertNotNull(expensesPerCategory);
        assertEquals(1, expensesPerCategory.size());
        assertEquals(100.0, expensesPerCategory.get(Category.GROCERIES));
        verify(expenseRepository, times(1)).findAll(any(Specification.class));
    }
}
