package com.popov.fintrack.report.formatter.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.report.formatter.PageNumberEvent;
import com.popov.fintrack.summary.dto.YearlySummary;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YearlyPdfFormatter extends BasePdfFormatter {

    private final WalletService walletService;

    public byte[] format(YearlySummary yearlySummary) {

        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setPageEvent(new PageNumberEvent());
            document.open();

            addTitle(document, "Financial Report (" + yearlySummary.getYear() + ")");
            Wallet wallet = walletService.getWalletById(yearlySummary.getWalletId());
            addWalletInfo(document, wallet);
            addAnnualSummary(document, yearlySummary);
            addMonthlyBreakdown(document, yearlySummary);
            addMetricTable(document, yearlySummary);
            document.newPage();
            addTitle(document, "Expense Summary");
            addExpenseSummary(document, yearlySummary);
            document.newPage();
            addTitle(document, "Income Summary");
            addIncomeSummary(document, yearlySummary);

            document.close();

            return out.toByteArray();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Error occurred when generating PDF");
        }
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
            addCell(table, month.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            addCell(table, yearlySummary.getExpensesPerMonth().getOrDefault(month, 0.0).toString());
            addCell(table, yearlySummary.getIncomesPerMonth().getOrDefault(month, 0.0).toString());
        }
        document.add(table);
    }

    private void addMetricTable(Document document, YearlySummary yearlySummary) throws DocumentException {
        PdfPTable metricTable = createTable(2, 1, 1);
        addTableHeader(metricTable, "Metric", "Month");

        addCell(metricTable, "Highest Income");
        Month highestIncomeMonth = yearlySummary.getHighestIncomeMonth();
        String highestIncomeMonthDisplay = highestIncomeMonth != null ? highestIncomeMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH) : "Not Available";
        addCell(metricTable, highestIncomeMonthDisplay);

        addCell(metricTable, "Lowest Income");
        Month lowestIncomeMonth = yearlySummary.getLowestIncomeMonth();
        String lowestIncomeMonthDisplay = lowestIncomeMonth != null ? lowestIncomeMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH) : "Not Available";
        addCell(metricTable, lowestIncomeMonthDisplay);

        addCell(metricTable, "Highest Expense");
        Month mostExpensiveMonth = yearlySummary.getMostExpensiveMonth();
        String mostExpensiveMonthDisplay = mostExpensiveMonth != null ? mostExpensiveMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH) : "Not Available";
        addCell(metricTable, mostExpensiveMonthDisplay);

        addCell(metricTable, "Lowest Expense");
        Month leastExpensiveMonth = yearlySummary.getLeastExpensiveMonth();
        String leastExpensiveMonthDisplay = leastExpensiveMonth != null ? leastExpensiveMonth.getDisplayName(TextStyle.FULL, Locale.ENGLISH) : "Not Available";
        addCell(metricTable, leastExpensiveMonthDisplay);

        document.add(metricTable);
    }

    private void addExpenseSummary(Document document, YearlySummary yearlySummary) throws DocumentException {
        document.add(createStyledParagraph("Expenses per Category:"));
        PdfPTable expenseSummaryTable = createTable(2, 1, 1);
        addTableHeader(expenseSummaryTable, "Category", "Amount");
        for (Map.Entry<Category, Double> entry : yearlySummary.getExpensesPerCategory().entrySet()) {
            addCell(expenseSummaryTable, entry.getKey().getDisplayName());
            addCell(expenseSummaryTable, String.format("%.2f", entry.getValue()));
        }
        document.add(expenseSummaryTable);
    }

    private void addIncomeSummary(Document document, YearlySummary yearlySummary) throws DocumentException {
        document.add(createStyledParagraph("Income per Category:"));
        PdfPTable incomeSummaryTable = createTable(2, 1, 1);
        addTableHeader(incomeSummaryTable, "Category", "Amount");
        for (Map.Entry<Category, Double> entry : yearlySummary.getIncomePerCategory().entrySet()) {
            addCell(incomeSummaryTable, entry.getKey().getDisplayName());
            addCell(incomeSummaryTable, String.format("%.2f", entry.getValue()));
        }
        document.add(incomeSummaryTable);
    }
}
