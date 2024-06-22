package com.popov.fintrack.report.formatter.xlsx;

import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.model.Category;
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
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YearlyXlsxFormatter extends BaseXlsxFormatter {

    private final WalletService walletService;

    public byte[] format(YearlySummary yearlySummary) {
        try (Workbook workbook = new XSSFWorkbook()) {

            createWalletInfoSheet(workbook, yearlySummary.getWalletId(), walletService);
            createYearlySummarySheet(workbook, yearlySummary);
            createExpensePerMonthSheet(workbook, yearlySummary.getExpensesPerMonth());
            createIncomePerMonthSheet(workbook, yearlySummary.getIncomesPerMonth());
            createExpensePerCategorySheet(workbook, yearlySummary.getExpensesPerCategory());
            createIncomePerCategorySheet(workbook, yearlySummary.getIncomePerCategory());

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new ReportGenerationException("Failed to generate Excel report");
        }
    }

    private void createYearlySummarySheet(Workbook workbook, YearlySummary yearlySummary) {
        CellStyle headerStyle = getHeaderCellStyle(workbook);
        CellStyle dataStyle = getDataCellStyle(workbook);

        Sheet sheet = workbook.createSheet("Yearly Summary");

        String[] headers1 = {"Year", "Total Expenses", "Most Expensive Month",
                "Least Expensive Month", "Average Monthly Expense"};

        String[] headers2 = {"Total Income", "Highest Income Month",
                "Lowest Income Month", "Average Monthly Income"};

        int rowIdx = 0;

        Row headerRow1 = sheet.createRow(rowIdx++);
        for (int i = 0; i < headers1.length; i++) {
            createHeaderCell(headerRow1, i, headers1[i], headerStyle);
        }

        Row dataRow1 = sheet.createRow(rowIdx++);
        createDataCell(dataRow1, 0, yearlySummary.getYear(), dataStyle);
        createDataCell(dataRow1, 1, yearlySummary.getTotalExpenses(), dataStyle);
        createDataCell(dataRow1, 2, yearlySummary.getMostExpensiveMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), dataStyle);
        createDataCell(dataRow1, 3, yearlySummary.getLeastExpensiveMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), dataStyle);
        createDataCell(dataRow1, 4, yearlySummary.getAverageMonthlyExpense(), dataStyle);

        sheet.createRow(rowIdx++);
        sheet.createRow(rowIdx++);

        Row headerRow2 = sheet.createRow(rowIdx++);
        for (int i = 0; i < headers2.length; i++) {
            createHeaderCell(headerRow2, i, headers2[i], headerStyle);
        }

        Row dataRow2 = sheet.createRow(rowIdx);
        createDataCell(dataRow2, 0, yearlySummary.getTotalIncome(), dataStyle);
        createDataCell(dataRow2, 1, yearlySummary.getHighestIncomeMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), dataStyle);
        createDataCell(dataRow2, 2, yearlySummary.getLowestIncomeMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), dataStyle);
        createDataCell(dataRow2, 3, yearlySummary.getAverageMonthlyIncome(), dataStyle);

        autoSizeColumns(sheet, 5);
    }

    private void createExpensePerMonthSheet(Workbook workbook, Map<Month, Double> map) {
        createPerMonthSheet(workbook, map, BaseXlsxFormatter.EXPENSES_PER_MONTH);
    }

    private void createIncomePerMonthSheet(Workbook workbook, Map<Month, Double> incomesPerMonth) {
        createPerMonthSheet(workbook, incomesPerMonth, BaseXlsxFormatter.INCOMES_PER_MONTH);
    }

    private void createPerMonthSheet(Workbook workbook, Map<Month, Double> map, String sheetName) {
        CellStyle headerStyle = getHeaderCellStyle(workbook);
        CellStyle dataStyle = getDataCellStyle(workbook);

        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Month", "Total"};

        for (int i = 0; i < headers.length; i++) {
            createHeaderCell(headerRow, i, headers[i], headerStyle);
        }

        int rowNum = 1;
        for (Map.Entry<Month, Double> entry : map.entrySet()) {
            Row dataRow = sheet.createRow(rowNum++);
            createDataCell(dataRow, 0, entry.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH), dataStyle);
            createDataCell(dataRow, 1, entry.getValue(), dataStyle);
        }

        autoSizeColumns(sheet, headers.length);
    }

    private void createExpensePerCategorySheet(Workbook workbook, Map<Category, Double> map) {
        createPerCategorySheet(workbook, map, BaseXlsxFormatter.EXPENSES_PER_CATEGORY);
    }

    private void createIncomePerCategorySheet(Workbook workbook, Map<Category, Double> incomePerCategory) {
        createPerCategorySheet(workbook, incomePerCategory, BaseXlsxFormatter.INCOMES_PER_CATEGORY);
    }

    private void createPerCategorySheet(Workbook workbook, Map<Category, Double> map, String sheetName) {
        CellStyle headerStyle = getHeaderCellStyle(workbook);
        CellStyle dataStyle = getDataCellStyle(workbook);

        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Category", "Total"};

        for (int i = 0; i < headers.length; i++) {
            createHeaderCell(headerRow, i, headers[i], headerStyle);
        }

        int rowNum = 1;
        for (Map.Entry<Category, Double> entry : map.entrySet()) {
            Row dataRow = sheet.createRow(rowNum++);
            createDataCell(dataRow, 0, entry.getKey().getDisplayName(), dataStyle);
            createDataCell(dataRow, 1, entry.getValue(), dataStyle);
        }

        autoSizeColumns(sheet, headers.length);
    }

}
