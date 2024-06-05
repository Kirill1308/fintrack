package com.popov.fintrack.report.utils;

import com.popov.fintrack.summary.SummaryDataFetcher;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.report.formatter.CsvFormatter;
import com.popov.fintrack.report.formatter.PdfFormatter;
import com.popov.fintrack.report.formatter.XlsFormatter;
import com.popov.fintrack.transaction.dto.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Month;

@Component
@RequiredArgsConstructor
public class ReportGenerator {

    private final SummaryDataFetcher summaryDataFetcher;

    private final PdfFormatter pdfFormatter;
    private final CsvFormatter csvFormatter;
    private final XlsFormatter xlsFormatter;

    public byte[] createYearlyPdfReport(int year, Long walletId) {
        YearlySummary yearlySummary = summaryDataFetcher.fetchYearlyData(year, walletId);
        return pdfFormatter.format(yearlySummary);
    }

    public byte[] createYearlyCsvReport(int year, Long walletId) {
        YearlySummary yearlySummary = summaryDataFetcher.fetchYearlyData(year, walletId);
        return csvFormatter.format(yearlySummary);
    }

    public byte[] createMonthlyPdfReport(int year, Month month, Long walletId) {
        MonthlySummary monthlySummary = summaryDataFetcher.fetchMonthlyData(year, month, walletId);
        return pdfFormatter.format(monthlySummary);
    }

    public byte[] createMonthlyCsvReport(int year, Month month, Long walletId) {
        MonthlySummary monthlySummary = summaryDataFetcher.fetchMonthlyData(year, month, walletId);
        return csvFormatter.format(monthlySummary);
    }

    public byte[] createYearlyXlsxReport(int year, Long walletId) {
        YearlySummary yearlySummary = summaryDataFetcher.fetchYearlyData(year, walletId);
        return xlsFormatter.format(yearlySummary);
    }

    public byte[] createMonthlyXlsxReport(int year, Month month, Long walletId) {
        MonthlySummary monthlySummary = summaryDataFetcher.fetchMonthlyData(year, month, walletId);
        return xlsFormatter.format(monthlySummary);
    }

    public byte[] createCustomPdfReport(DateRange dateRange, Long walletId) {
        return null;
    }

    public byte[] createCustomCsvReport(DateRange dateRange, Long walletId) {
        return null;
    }

    public byte[] createCustomXlsxReport(DateRange dateRange, Long walletId) {
        return null;
    }
}
