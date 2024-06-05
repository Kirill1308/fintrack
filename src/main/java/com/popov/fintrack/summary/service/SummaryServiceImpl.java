package com.popov.fintrack.summary.service;

import com.popov.fintrack.summary.SummaryDataFetcher;
import com.popov.fintrack.summary.SummaryService;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.FinancialSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.FilterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryServiceImpl implements SummaryService {

    private final SummaryDataFetcher summaryFetcher;

    @Override
    public FinancialSummary getFinancialSummary(FilterDTO filters) {
        return summaryFetcher.fetchFinancialSummary(filters);
    }

    @Override
    public CustomSummary getCustomSummary(FilterDTO filters) {
        return summaryFetcher.fetchCustomData(filters);
    }

    @Override
    public YearlySummary getYearlySummary(int year, Long walletId) {
        return summaryFetcher.fetchYearlyData(year, walletId);
    }

    @Override
    public MonthlySummary getMonthlySummary(int year, Month month, Long walletId) {
        return summaryFetcher.fetchMonthlyData(year, month, walletId);
    }
}
