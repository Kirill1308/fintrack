package com.popov.fintrack.report.utils;

import com.popov.fintrack.report.formatter.PdfFormatter;
import com.popov.fintrack.report.formatter.XlsxFormatter;
import com.popov.fintrack.summary.SummaryDataFetcher;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.dto.FilterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;

import static com.popov.fintrack.report.ReportTestData.customSummary;
import static com.popov.fintrack.report.ReportTestData.dateRange;
import static com.popov.fintrack.report.ReportTestData.monthlySummary;
import static com.popov.fintrack.report.ReportTestData.yearlySummary;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportGeneratorTest {

    @Mock
    private SummaryDataFetcher summaryDataFetcher;

    @Mock
    private PdfFormatter pdfFormatter;

    @Mock
    private XlsxFormatter xlsxFormatter;

    @InjectMocks
    private ReportGenerator reportGenerator;

    @Test
    void createYearlyPdfReport_success() {
        when(summaryDataFetcher.fetchYearlyData(anyInt(), anyLong())).thenReturn(yearlySummary);
        when(pdfFormatter.format(any(YearlySummary.class))).thenReturn(new byte[0]);

        byte[] report = reportGenerator.createYearlyPdfReport(2024, 1L);

        assertNotNull(report);
        verify(summaryDataFetcher, times(1)).fetchYearlyData(2024, 1L);
        verify(pdfFormatter, times(1)).format(yearlySummary);
    }

    @Test
    void createMonthlyPdfReport_success() {
        when(summaryDataFetcher.fetchMonthlyData(anyInt(), any(Month.class), anyLong())).thenReturn(monthlySummary);
        when(pdfFormatter.format(any(MonthlySummary.class))).thenReturn(new byte[0]);

        byte[] report = reportGenerator.createMonthlyPdfReport(2024, Month.JANUARY, 1L);

        assertNotNull(report);
        verify(summaryDataFetcher, times(1)).fetchMonthlyData(2024, Month.JANUARY, 1L);
        verify(pdfFormatter, times(1)).format(monthlySummary);
    }

    @Test
    void createYearlyXlsxReport_success() {
        when(summaryDataFetcher.fetchYearlyData(anyInt(), anyLong())).thenReturn(yearlySummary);
        when(xlsxFormatter.format(any(YearlySummary.class))).thenReturn(new byte[0]);

        byte[] report = reportGenerator.createYearlyXlsxReport(2024, 1L);

        assertNotNull(report);
        verify(summaryDataFetcher, times(1)).fetchYearlyData(2024, 1L);
        verify(xlsxFormatter, times(1)).format(yearlySummary);
    }

    @Test
    void createMonthlyXlsxReport_success() {
        when(summaryDataFetcher.fetchMonthlyData(anyInt(), any(Month.class), anyLong())).thenReturn(monthlySummary);
        when(xlsxFormatter.format(any(MonthlySummary.class))).thenReturn(new byte[0]);

        byte[] report = reportGenerator.createMonthlyXlsxReport(2024, Month.JANUARY, 1L);

        assertNotNull(report);
        verify(summaryDataFetcher, times(1)).fetchMonthlyData(2024, Month.JANUARY, 1L);
        verify(xlsxFormatter, times(1)).format(monthlySummary);
    }

    @Test
    void createCustomPdfReport_success() {
        when(summaryDataFetcher.fetchCustomData(any(FilterDTO.class))).thenReturn(customSummary);
        when(pdfFormatter.format(any(CustomSummary.class))).thenReturn(new byte[0]);

        byte[] report = reportGenerator.createCustomPdfReport(dateRange, 1L);

        assertNotNull(report);
        verify(summaryDataFetcher, times(1)).fetchCustomData(any(FilterDTO.class));
        verify(pdfFormatter, times(1)).format(customSummary);
    }

    @Test
    void createCustomXlsxReport_success() {
        when(summaryDataFetcher.fetchCustomData(any(FilterDTO.class))).thenReturn(customSummary);
        when(xlsxFormatter.format(any(CustomSummary.class))).thenReturn(new byte[0]);

        byte[] report = reportGenerator.createCustomXlsxReport(dateRange, 1L);

        assertNotNull(report);
        verify(summaryDataFetcher, times(1)).fetchCustomData(any(FilterDTO.class));
        verify(xlsxFormatter, times(1)).format(customSummary);
    }
}
