package com.popov.fintrack.report.service.impl;

import com.popov.fintrack.report.repository.IncomeRepository;
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
class IncomeServiceImplTest {

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeServiceImpl incomeService;

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
    void getTotalFilteredIncome_success() {
        when(incomeRepository.findAll(any(Specification.class))).thenReturn(transactionList);

        FilterDTO filterDTO = new FilterDTO();
        Double totalIncome = incomeService.getTotalFilteredIncome(filterDTO);

        assertEquals(100.0, totalIncome);
        verify(incomeRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getTotalIncomeForYear_success() {
        when(incomeRepository.getTotalIncomeForYear(2024, 1L)).thenReturn(1200.0);

        Double totalIncome = incomeService.getTotalIncomeForYear(2024, 1L);

        assertEquals(1200.0, totalIncome);
        verify(incomeRepository, times(1)).getTotalIncomeForYear(2024, 1L);
    }

    @Test
    void getHighestIncomeMonth_success() {
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{1, 500.0});

        when(incomeRepository.getHighestIncomeMonth(2024, 1L)).thenReturn(results);

        Month month = incomeService.getHighestIncomeMonth(2024, 1L);

        assertEquals(Month.JANUARY, month);
        verify(incomeRepository, times(1)).getHighestIncomeMonth(2024, 1L);
    }

    @Test
    void getLowestIncomeMonth_success() {
        List<Object[]> results = new ArrayList<>();
        results.add(new Object[]{2, 300.0});

        when(incomeRepository.getLowestIncomeMonth(2024, 1L)).thenReturn(results);

        Month month = incomeService.getLowestIncomeMonth(2024, 1L);

        assertEquals(Month.FEBRUARY, month);
        verify(incomeRepository, times(1)).getLowestIncomeMonth(2024, 1L);
    }

    @Test
    void getAverageMonthlyIncome_success() {
        when(incomeRepository.getAverageMonthlyIncome(2024, 1L)).thenReturn(100.0);

        Double averageIncome = incomeService.getAverageMonthlyIncome(2024, 1L);

        assertEquals(100.0, averageIncome);
        verify(incomeRepository, times(1)).getAverageMonthlyIncome(2024, 1L);
    }

    @Test
    void getIncomesPerMonth_success() {
        when(incomeRepository.getIncomesPerMonth(2024, 1L)).thenReturn(List.of(new Object[]{1, 500.0}, new Object[]{2, 300.0}));

        Map<Month, Double> incomesPerMonth = incomeService.getIncomesPerMonth(2024, 1L);

        assertNotNull(incomesPerMonth);
        assertEquals(2, incomesPerMonth.size());
        assertEquals(500.0, incomesPerMonth.get(Month.JANUARY));
        assertEquals(300.0, incomesPerMonth.get(Month.FEBRUARY));
        verify(incomeRepository, times(1)).getIncomesPerMonth(2024, 1L);
    }

    @Test
    void getIncomePerCategory_success() {
        when(incomeRepository.getIncomePerCategory(2024, 1L)).thenReturn(List.of(new Object[]{Category.GROCERIES, 200.0}, new Object[]{Category.RENT, 800.0}));

        Map<Category, Double> incomePerCategory = incomeService.getIncomePerCategory(2024, 1L);

        assertNotNull(incomePerCategory);
        assertEquals(2, incomePerCategory.size());
        assertEquals(200.0, incomePerCategory.get(Category.GROCERIES));
        assertEquals(800.0, incomePerCategory.get(Category.RENT));
        verify(incomeRepository, times(1)).getIncomePerCategory(2024, 1L);
    }

    @Test
    void getTotalIncomeForMonth_success() {
        when(incomeRepository.getTotalIncomeForMonth(2024, 1, 1L)).thenReturn(100.0);

        Double totalIncome = incomeService.getTotalIncomeForMonth(2024, Month.JANUARY, 1L);

        assertEquals(100.0, totalIncome);
        verify(incomeRepository, times(1)).getTotalIncomeForMonth(2024, 1, 1L);
    }

    @Test
    void getAverageDailyIncome_success() {
        when(incomeRepository.getTotalIncomeForMonth(2024, 1, 1L)).thenReturn(3100.0);

        Double averageDailyIncome = incomeService.getAverageDailyIncome(2024, Month.JANUARY, 1L);

        assertEquals(100.0, averageDailyIncome);
        verify(incomeRepository, times(1)).getTotalIncomeForMonth(2024, 1, 1L);
    }

    @Test
    void getLowestDailyIncome_success() {
        when(incomeRepository.getLowestDailyIncome(2024, 1, 1L)).thenReturn(50.0);

        Double lowestDailyIncome = incomeService.getLowestDailyIncome(2024, Month.JANUARY, 1L);

        assertEquals(50.0, lowestDailyIncome);
        verify(incomeRepository, times(1)).getLowestDailyIncome(2024, 1, 1L);
    }

    @Test
    void getAverageDailyIncome_FilterDTO_success() {
        when(incomeRepository.findAll(any(Specification.class))).thenReturn(transactionList);

        FilterDTO filterDTO = new FilterDTO();
        Double averageDailyIncome = incomeService.getAverageDailyIncome(filterDTO);

        assertEquals(100.0, averageDailyIncome);
        verify(incomeRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getIncomesPerDate_success() {
        when(incomeRepository.findAll(any(Specification.class))).thenReturn(transactionList);

        FilterDTO filterDTO = new FilterDTO();
        Map<LocalDate, Double> incomesPerDate = incomeService.getIncomesPerDate(filterDTO);

        assertNotNull(incomesPerDate);
        assertEquals(1, incomesPerDate.size());
        assertEquals(100.0, incomesPerDate.get(transaction.getDateCreated()));
        verify(incomeRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void getIncomePerCategory_FilterDTO_success() {
        when(incomeRepository.getIncomePerCategory(any(Specification.class))).thenReturn(List.of(new Object[]{Category.GROCERIES, 200.0}, new Object[]{Category.RENT, 800.0}));

        FilterDTO filterDTO = new FilterDTO();
        Map<Category, Double> incomePerCategory = incomeService.getIncomePerCategory(filterDTO);

        assertNotNull(incomePerCategory);
        assertEquals(2, incomePerCategory.size());
        assertEquals(200.0, incomePerCategory.get(Category.GROCERIES));
        assertEquals(800.0, incomePerCategory.get(Category.RENT));
        verify(incomeRepository, times(1)).getIncomePerCategory(any(Specification.class));
    }
}
