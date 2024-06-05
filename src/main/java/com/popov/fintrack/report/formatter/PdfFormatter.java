package com.popov.fintrack.report.formatter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Month;
import java.util.Map;

@Service
public class PdfFormatter implements ReportFormatter {

    private static final Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0, 102, 204));
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static final BaseColor headerBgColor = new BaseColor(0, 102, 204);

    @Override
    public byte[] format(YearlySummary yearlySummary) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            addTitle(document, "Financial Report (" + yearlySummary.getYear() + ")");
            String accountHolder = SecurityUtils.getAuthenticatedUser().getName();
            addAccountHolder(document, accountHolder);
            addAnnualSummary(document, yearlySummary);
            addMonthlyBreakdown(document, yearlySummary);
            addMetricTable(document, yearlySummary);
            document.newPage();
            addTitle(document, "Expense Summary");
            addExpenseSummary(document, yearlySummary);

            document.close();

            return out.toByteArray();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Error occurred when generating PDF");
        }
    }

    @Override
    public byte[] format(MonthlySummary monthlySummary) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            addTitle(document, "Financial Report (" + monthlySummary.getMonth().name() + " " + monthlySummary.getYear() + ")");
            String accountHolder = SecurityUtils.getAuthenticatedUser().getName();
            addAccountHolder(document, accountHolder);

            addMonthlyTotals(document, monthlySummary);
            addMonthlyAverages(document, monthlySummary);
            addMonthlyBalance(document, monthlySummary);

            document.close();

            return out.toByteArray();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Error occurred when generating PDF");
        }
    }

    private void addMonthlyTotals(Document document, MonthlySummary monthlySummary) throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Monthly totals:", titleFont));
        document.add(new Paragraph("Total expenses: " + monthlySummary.getTotalExpenses(), bodyFont));
        document.add(new Paragraph("Total income: " + monthlySummary.getTotalIncome(), bodyFont));
    }

    private void addMonthlyAverages(Document document, MonthlySummary monthlySummary) throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Monthly averages:", titleFont));
        document.add(new Paragraph("Average daily expense: " + String.format("%.2f", monthlySummary.getAverageDailyExpense()), bodyFont));
        document.add(new Paragraph("Average daily income: " + String.format("%.2f", monthlySummary.getAverageDailyIncome()), bodyFont));
    }

    private void addMonthlyBalance(Document document, MonthlySummary monthlySummary) throws DocumentException {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Monthly balance:", titleFont));
        document.add(new Paragraph("Average daily balance: " + String.format("%.2f", monthlySummary.getAverageDailyBalance()), bodyFont));
    }

    private void addTitle(Document document, String titleText) throws DocumentException {
        Paragraph title = createTitle(titleText);
        document.add(new Paragraph("\n"));
        document.add(title);
    }

    private void addAccountHolder(Document document, String accountHolder) throws DocumentException {
        document.add(new Paragraph("Account Holder: " + accountHolder, bodyFont));
        document.add(new Paragraph("\n"));
    }

    private void addAnnualSummary(Document document, YearlySummary yearlySummary) throws DocumentException {
        document.add(createStyledParagraph("Annual Summary:"));
        document.add(new Paragraph("- Total Income: " + yearlySummary.getTotalIncome(), bodyFont));
        document.add(new Paragraph("- Total Expenses: " + yearlySummary.getTotalExpenses(), bodyFont));
        document.add(new Paragraph("\n"));
    }

    private void addMonthlyBreakdown(Document document, YearlySummary yearlySummary) throws DocumentException {
        document.add(new Paragraph("Detailed Monthly Breakdown:", bodyFont));
        PdfPTable table = createTable(3, 2, 1, 1);
        addTableHeader(table, "Month", "Expenses", "Income");
        for (Month month : Month.values()) {
            addCell(table, month.name());
            addCell(table, yearlySummary.getExpensesPerMonth().getOrDefault(month, Double.valueOf(0.0)).toString());
            addCell(table, yearlySummary.getIncomesPerMonth().getOrDefault(month, Double.valueOf(0.0)).toString());
        }
        document.add(table);
    }

    private void addMetricTable(Document document, YearlySummary yearlySummary) throws DocumentException {
        PdfPTable metricTable = createTable(2, 1, 1);
        addTableHeader(metricTable, "Metric", "Month");
        addCell(metricTable, "Highest Income");
        addCell(metricTable, yearlySummary.getHighestIncomeMonth().name());
        addCell(metricTable, "Lowest Income");
        addCell(metricTable, yearlySummary.getLowestIncomeMonth().name());
        addCell(metricTable, "Highest Expense");
        addCell(metricTable, yearlySummary.getMostExpensiveMonth().name());
        addCell(metricTable, "Lowest Expense");
        addCell(metricTable, yearlySummary.getLeastExpensiveMonth().name());
        document.add(metricTable);
    }

    private void addExpenseSummary(Document document, YearlySummary yearlySummary) throws DocumentException {
        document.add(createStyledParagraph("Expenses per Category:"));
        PdfPTable expenseSummaryTable = createTable(2, 1, 1);
        addTableHeader(expenseSummaryTable, "Category", "Amount");
        for (Map.Entry<Category, Double> entry : yearlySummary.getExpensesPerCategory().entrySet()) {
            addCell(expenseSummaryTable, entry.getKey().name());
            addCellRightAligned(expenseSummaryTable, String.format("%.2f", entry.getValue()));
        }
        document.add(expenseSummaryTable);
    }

    public Paragraph createTitle(String titleText) {
        Paragraph title = new Paragraph(titleText, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        return title;
    }

    public PdfPTable createTable(int numColumns, float... widths) throws DocumentException {
        PdfPTable table = new PdfPTable(numColumns);
        table.setWidthPercentage(100);
        table.setWidths(widths);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        return table;
    }

    public void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(headerBgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }

    public void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    public void addCellRightAligned(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5);
        table.addCell(cell);
    }

    public Paragraph createStyledParagraph(String content) {
        Paragraph paragraph = new Paragraph(content, bodyFont);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        return paragraph;
    }
}
