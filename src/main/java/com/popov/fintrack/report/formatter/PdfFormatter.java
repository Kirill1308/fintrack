package com.popov.fintrack.report.formatter;

import com.popov.fintrack.report.formatter.pdf.CustomPdfFormatter;
import com.popov.fintrack.report.formatter.pdf.MonthlyPdfFormatter;
import com.popov.fintrack.report.formatter.pdf.YearlyPdfFormatter;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfFormatter implements ReportFormatter {

    private final YearlyPdfFormatter yearlyPdfFormatter;
    private final MonthlyPdfFormatter monthlyPdfFormatter;
    private final CustomPdfFormatter customPdfFormatter;

    @Override
    public byte[] format(YearlySummary yearlySummary) {
        return yearlyPdfFormatter.format(yearlySummary);
    }

    @Override
    public byte[] format(MonthlySummary monthlySummary) {
        return monthlyPdfFormatter.format(monthlySummary);
    }

    @Override
    public byte[] format(CustomSummary customSummary) {
        return customPdfFormatter.format(customSummary);
    }
}
