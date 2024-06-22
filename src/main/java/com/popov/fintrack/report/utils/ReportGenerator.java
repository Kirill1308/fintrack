package com.popov.fintrack.report.utils;

import com.popov.fintrack.report.formatter.PdfFormatter;
import com.popov.fintrack.report.formatter.XlsxFormatter;
import com.popov.fintrack.summary.SummaryDataFetcher;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.DateRange;
import com.popov.fintrack.transaction.dto.FilterDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportGenerator {

    private final SummaryDataFetcher summaryDataFetcher;

    private final PdfFormatter pdfFormatter;
    private final XlsxFormatter xlsxFormatter;

    public byte[] createYearlyPdfReport(int year, Long walletId) {
        YearlySummary yearlySummary = summaryDataFetcher.fetchYearlyData(year, walletId);
        return pdfFormatter.format(yearlySummary);
    }

    public byte[] createMonthlyPdfReport(int year, Month month, Long walletId) {
        MonthlySummary monthlySummary = summaryDataFetcher.fetchMonthlyData(year, month, walletId);
        return pdfFormatter.format(monthlySummary);
    }

    public byte[] createYearlyXlsxReport(int year, Long walletId) {
        YearlySummary yearlySummary = summaryDataFetcher.fetchYearlyData(year, walletId);
        return xlsxFormatter.format(yearlySummary);
    }

    public byte[] createMonthlyXlsxReport(int year, Month month, Long walletId) {
        MonthlySummary monthlySummary = summaryDataFetcher.fetchMonthlyData(year, month, walletId);
        return xlsxFormatter.format(monthlySummary);
    }

    public byte[] createCustomPdfReport(DateRange dateRange, Long walletId) {
        FilterDTO filters = new FilterDTO();
        filters.setDateRange(dateRange);
        filters.setWalletIds(List.of(walletId));
        CustomSummary customSummary = summaryDataFetcher.fetchCustomData(filters);
        return pdfFormatter.format(customSummary);
    }

    public byte[] createCustomXlsxReport(DateRange dateRange, Long walletId) {
        FilterDTO filters = new FilterDTO();
        filters.setDateRange(dateRange);
        filters.setWalletIds(List.of(walletId));
        CustomSummary customSummary = summaryDataFetcher.fetchCustomData(filters);
        return xlsxFormatter.format(customSummary);
    }
}
