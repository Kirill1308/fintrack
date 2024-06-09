package com.popov.fintrack.report.formatter.xlsx;

import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MonthlyXlsxFormatter extends BaseXlsxFormatter {

    private final WalletService walletService;

    public byte[] format(MonthlySummary monthlySummary) {
        try (Workbook workbook = new XSSFWorkbook()) {
            createWalletInfoSheet(workbook, monthlySummary.getWalletId(), walletService);
            createMonthlySummarySheet(workbook, monthlySummary);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }

        } catch (IOException e) {
            throw new ReportGenerationException("Failed to generate Excel report");
        }
    }

    private void createMonthlySummarySheet(Workbook workbook, MonthlySummary monthlySummary) {
        CellStyle headerStyle = getHeaderCellStyle(workbook);
        CellStyle dataStyle = getDataCellStyle(workbook);

        Sheet sheet = workbook.createSheet("Monthly Report");

        Row headerRow = sheet.createRow(0);
        createHeaderCell(headerRow, 0, "Year", headerStyle);
        createHeaderCell(headerRow, 1, "Month", headerStyle);
        createHeaderCell(headerRow, 2, "Total Expenses", headerStyle);
        createHeaderCell(headerRow, 3, "Total Income", headerStyle);
        createHeaderCell(headerRow, 4, "Average Daily Expense", headerStyle);
        createHeaderCell(headerRow, 5, "Average Daily Income", headerStyle);
        createHeaderCell(headerRow, 6, "Average Daily Balance", headerStyle);
        createHeaderCell(headerRow, 7, "Net Savings", headerStyle);
        createHeaderCell(headerRow, 8, "Highest Daily Expense", headerStyle);
        createHeaderCell(headerRow, 9, "Lowest Daily Income", headerStyle);

        Row dataRow = sheet.createRow(1);
        createDataCell(dataRow, 0, monthlySummary.getYear(), dataStyle);
        createDataCell(dataRow, 1, monthlySummary.getMonth().name(), dataStyle);
        createDataCell(dataRow, 2, monthlySummary.getTotalExpenses(), dataStyle);
        createDataCell(dataRow, 3, monthlySummary.getTotalIncome(), dataStyle);
        createDataCell(dataRow, 4, monthlySummary.getAverageDailyExpense(), dataStyle);
        createDataCell(dataRow, 5, monthlySummary.getAverageDailyIncome(), dataStyle);
        createDataCell(dataRow, 6, monthlySummary.getAverageDailyBalance(), dataStyle);
        createDataCell(dataRow, 7, monthlySummary.getNetSavings(), dataStyle);
        createDataCell(dataRow, 8, monthlySummary.getHighestDailyExpense(), dataStyle);
        createDataCell(dataRow, 9, monthlySummary.getLowestDailyIncome(), dataStyle);

        autoSizeColumns(sheet, 10);
    }
}
