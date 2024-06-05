package com.popov.fintrack.report.formatter;

import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import org.springframework.stereotype.Component;

@Component
public class CsvFormatter implements ReportFormatter {
    public byte[] format(YearlySummary yearlySummary) {
        return null;
    }

    @Override
    public byte[] format(MonthlySummary monthlySummary) {
        return new byte[0];
    }
}
