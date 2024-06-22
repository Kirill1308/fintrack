package com.popov.fintrack.report.formatter;

import com.popov.fintrack.report.formatter.xlsx.CustomXlsxFormatter;
import com.popov.fintrack.report.formatter.xlsx.MonthlyXlsxFormatter;
import com.popov.fintrack.report.formatter.xlsx.YearlyXlsxFormatter;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class XlsxFormatter implements ReportFormatter {

    private final YearlyXlsxFormatter yearlyXlsxFormatter;
    private final MonthlyXlsxFormatter monthlyXlsxFormatter;
    private final CustomXlsxFormatter customXlsxFormatter;

    @Override
    public byte[] format(YearlySummary yearlySummary) {
        return yearlyXlsxFormatter.format(yearlySummary);
    }

    @Override
    public byte[] format(MonthlySummary monthlySummary) {
        return monthlyXlsxFormatter.format(monthlySummary);
    }

    @Override
    public byte[] format(CustomSummary customSummary) {
        return customXlsxFormatter.format(customSummary);
    }
}
