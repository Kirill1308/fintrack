package com.popov.fintrack.report.service;

import com.popov.fintrack.report.ReportType;
import com.popov.fintrack.report.utils.ReportGenerator;
import com.popov.fintrack.transaction.dto.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportGenerator reportGenerator;

    public byte[] createYearlyReport(String format, int year, Long walletId) {
        ReportType detectedFormat = ReportType.valueOf(format.toUpperCase());
        return switch (detectedFormat) {
            case PDF -> reportGenerator.createYearlyPdfReport(year, walletId);
            case XLSX -> reportGenerator.createYearlyXlsxReport(year, walletId);
        };
    }

    public byte[] createMonthlyReport(String format, int year, Month month, Long walletId) {
        ReportType detectedFormat = ReportType.valueOf(format.toUpperCase());
        return switch (detectedFormat) {
            case PDF -> reportGenerator.createMonthlyPdfReport(year, month, walletId);
            case XLSX -> reportGenerator.createMonthlyXlsxReport(year, month, walletId);
        };
    }

    public byte[] createCustomReport(String format, DateRange dateRange, Long walletId) {
        ReportType detectedFormat = ReportType.valueOf(format.toUpperCase());
        return switch (detectedFormat) {
            case PDF -> reportGenerator.createCustomPdfReport(dateRange, walletId);
            case XLSX -> reportGenerator.createCustomXlsxReport(dateRange, walletId);
        };
    }
}
