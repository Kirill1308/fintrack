package com.popov.fintrack.summary;

import com.popov.fintrack.MatcherFactory;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.FinancialSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;

import java.time.Month;

public class SummaryTestData {

    public static final MatcherFactory.Matcher<FinancialSummary> FINANCIAL_SUMMARY_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(FinancialSummary.class);
    public static final MatcherFactory.Matcher<CustomSummary> CUSTOM_SUMMARY_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(CustomSummary.class);
    public static final MatcherFactory.Matcher<YearlySummary> YEARLY_SUMMARY_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(YearlySummary.class);
    public static final MatcherFactory.Matcher<MonthlySummary> MONTHLY_SUMMARY_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(MonthlySummary.class);

    public static final FinancialSummary financialSummary;
    public static final CustomSummary customSummary;
    public static final YearlySummary yearlySummary;
    public static final MonthlySummary monthlySummary;

    static {
        financialSummary = FinancialSummary.builder()
                .currentBalance(1000.0)
                .totalChange(500.0)
                .totalExpenses(200.0)
                .totalIncome(700.0)
                .build();

        customSummary = CustomSummary.builder()
                .totalExpenses(200.0)
                .totalIncome(700.0)
                .build();

        yearlySummary = YearlySummary.builder()
                .year(2023)
                .walletId(1L)
                .build();

        monthlySummary = MonthlySummary.builder()
                .year(2023)
                .month(Month.JANUARY)
                .walletId(1L)
                .build();
    }
}
