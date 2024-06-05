package com.popov.fintrack.report.formatter;

import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Month;
import java.util.Map;

@Component
public class XlsFormatter implements ReportFormatter {
    // Sheet names
    private static final String EXPENSES_PER_MONTH = "Expenses per Month";
    private static final String INCOMES_PER_MONTH = "Incomes per Month";
    private static final String EXPENSES_PER_CATEGORY = "Expenses per Category";
    private static final String INCOMES_PER_CATEGORY = "Incomes per Category";

    @Override
    public byte[] format(YearlySummary yearlySummary) {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle headerStyle = getHeaderCellStyle(workbook);
            CellStyle dataStyle = getDataCellStyle(workbook);

            createSummarySheet(workbook, yearlySummary, headerStyle, dataStyle);
            createDataPerMonthSheet(workbook, yearlySummary.getExpensesPerMonth(), EXPENSES_PER_MONTH, headerStyle, dataStyle);
            createDataPerMonthSheet(workbook, yearlySummary.getIncomesPerMonth(), INCOMES_PER_MONTH, headerStyle, dataStyle);
            createDataPerCategorySheet(workbook, yearlySummary.getExpensesPerCategory(), EXPENSES_PER_CATEGORY, headerStyle, dataStyle);
            createDataPerCategorySheet(workbook, yearlySummary.getIncomePerCategory(), INCOMES_PER_CATEGORY, headerStyle, dataStyle);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new ReportGenerationException("Failed to generate Excel report");
        }
    }

    @Override
    public byte[] format(MonthlySummary monthlySummary) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Report");

            Row headerRow = sheet.createRow(0);

            headerRow.createCell(0).setCellValue("Year");
            headerRow.createCell(1).setCellValue("Month");
            headerRow.createCell(2).setCellValue("Total Expenses");
            headerRow.createCell(3).setCellValue("Total Income");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(monthlySummary.getYear());
            dataRow.createCell(1).setCellValue(monthlySummary.getMonth().name());
            dataRow.createCell(2).setCellValue(monthlySummary.getTotalExpenses());
            dataRow.createCell(3).setCellValue(monthlySummary.getTotalIncome());

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new ReportGenerationException("Failed to generate Excel report");
        }
    }

    private void createSummarySheet(Workbook workbook, YearlySummary yearlySummary,
                                    CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Yearly Summary");

        String[] headers1 = {"Year", "Total Expenses", "Most Expensive Month",
                "Least Expensive Month", "Average Monthly Expense"};

        String[] headers2 = {"Total Income", "Highest Income Month",
                "Lowest Income Month", "Average Monthly Income"};

        Row headerRow1 = sheet.createRow(0);
        for (int i = 0; i < headers1.length; i++) {
            Cell headerCell = headerRow1.createCell(i);
            headerCell.setCellValue(headers1[i]);
            headerCell.setCellStyle(headerStyle);
        }

        Row dataRow1 = sheet.createRow(1);
        dataRow1.createCell(0).setCellValue(yearlySummary.getYear());
        dataRow1.createCell(1).setCellValue(yearlySummary.getTotalExpenses());
        dataRow1.createCell(2).setCellValue(yearlySummary.getMostExpensiveMonth().toString());
        dataRow1.createCell(3).setCellValue(yearlySummary.getLeastExpensiveMonth().toString());
        dataRow1.createCell(4).setCellValue(yearlySummary.getAverageMonthlyExpense());

        for (int i = 0; i < headers1.length; i++) {
            dataRow1.getCell(i).setCellStyle(dataStyle);
            sheet.autoSizeColumn(i);
        }

        sheet.createRow(2);
        sheet.createRow(3);

        Row headerRow2 = sheet.createRow(4);
        for (int i = 0; i < headers2.length; i++) {
            Cell headerCell = headerRow2.createCell(i);
            headerCell.setCellValue(headers2[i]);
            headerCell.setCellStyle(headerStyle);
        }

        Row dataRow2 = sheet.createRow(5);
        dataRow2.createCell(0).setCellValue(yearlySummary.getTotalIncome());
        dataRow2.createCell(1).setCellValue(yearlySummary.getHighestIncomeMonth().toString());
        dataRow2.createCell(2).setCellValue(yearlySummary.getLowestIncomeMonth().toString());
        dataRow2.createCell(3).setCellValue(yearlySummary.getAverageMonthlyIncome());

        for (int i = 0; i < headers2.length; i++) {
            dataRow2.getCell(i).setCellStyle(dataStyle);
            sheet.autoSizeColumn(i);
        }
    }

    private void createDataPerMonthSheet(Workbook workbook, Map<Month, Double> map, String sheetName,
                                         CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Month", "Total"};

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Map.Entry<Month, Double> entry : map.entrySet()) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(entry.getKey().toString());
            dataRow.createCell(1).setCellValue(entry.getValue());

            dataRow.getCell(0).setCellStyle(dataStyle);
            dataRow.getCell(1).setCellStyle(dataStyle);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createDataPerCategorySheet(Workbook workbook, Map<?, Double> map, String sheetName,
                                            CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet(sheetName);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Category", "Total"};

        for (int i = 0; i < headers.length; i++) {
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headers[i]);
            headerCell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Map.Entry<?, Double> entry : map.entrySet()) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(entry.getKey().toString());
            dataRow.createCell(1).setCellValue(entry.getValue());

            dataRow.getCell(0).setCellStyle(dataStyle);
            dataRow.getCell(1).setCellStyle(dataStyle);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    private CellStyle getDataCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        return style;
    }
}
