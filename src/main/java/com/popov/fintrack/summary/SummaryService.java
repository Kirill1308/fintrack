package com.popov.fintrack.summary;

import com.popov.fintrack.summary.dto.FinancialSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.FilterDTO;

import java.time.Month;


public interface SummaryService {

    FinancialSummary getFinancialSummary(FilterDTO filters);

    CustomSummary getCustomSummary(FilterDTO filters);

    YearlySummary getYearlySummary(int year, Long walletId);

    MonthlySummary getMonthlySummary(int year, Month month, Long walletId);
}
