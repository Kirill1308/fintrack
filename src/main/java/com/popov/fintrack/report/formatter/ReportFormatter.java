package com.popov.fintrack.report.formatter;

import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;

public interface ReportFormatter {

    byte[] format(YearlySummary yearlySummary);

    byte[] format(MonthlySummary monthlySummary);

    byte[] format(CustomSummary customSummary);
}
