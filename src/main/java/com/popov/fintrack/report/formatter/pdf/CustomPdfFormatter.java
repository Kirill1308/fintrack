package com.popov.fintrack.report.formatter.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.report.formatter.PageNumberEvent;
import com.popov.fintrack.summary.dto.CustomSummary;
import com.popov.fintrack.transaction.model.Category;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomPdfFormatter extends BasePdfFormatter {

    private final WalletService walletService;

    public byte[] format(CustomSummary customSummary) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setPageEvent(new PageNumberEvent());
            document.open();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
            addTitle(document, "Custom Financial Report (" + customSummary.getStartDate().format(formatter) + " - " + customSummary.getEndDate().format(formatter) + ")");
            Wallet wallet = walletService.getWalletById(customSummary.getWalletId());
            addWalletInfo(document, wallet);
            addCustomSummary(document, customSummary);
            document.newPage();
            addTitle(document, "Expense Summary");
            addCustomExpenseSummary(document, customSummary);
            document.newPage();
            addTitle(document, "Income Summary");
            addCustomIncomeSummary(document, customSummary);

            document.close();

            return out.toByteArray();
        } catch (DocumentException e) {
            throw new ReportGenerationException("Error occurred when generating PDF");
        }
    }

    private void addCustomSummary(Document document, CustomSummary customSummary) throws DocumentException {
        document.add(createStyledParagraph("Custom Summary:"));
        document.add(new Paragraph("- Total Income: " + customSummary.getTotalIncome(), bodyFont));
        document.add(new Paragraph("- Total Expenses: " + customSummary.getTotalExpenses(), bodyFont));
        document.add(new Paragraph("- Average Daily Expense: " + String.format("%.2f", customSummary.getAverageDailyExpense()), bodyFont));
        document.add(new Paragraph("- Average Daily Income: " + String.format("%.2f", customSummary.getAverageDailyIncome()), bodyFont));
        document.add(new Paragraph("\n"));
    }

    private void addCustomExpenseSummary(Document document, CustomSummary customSummary) throws DocumentException {
        document.add(createStyledParagraph("Expenses per Category:"));
        PdfPTable expenseSummaryTable = createTable(2, 1, 1);
        addTableHeader(expenseSummaryTable, "Category", "Amount");
        for (Map.Entry<Category, Double> entry : customSummary.getExpensesPerCategory().entrySet()) {
            addCell(expenseSummaryTable, entry.getKey().getDisplayName());
            addCell(expenseSummaryTable, String.format("%.2f", entry.getValue()));
        }
        document.add(expenseSummaryTable);
    }

    private void addCustomIncomeSummary(Document document, CustomSummary customSummary) throws DocumentException {
        document.add(createStyledParagraph("Income per Category:"));
        PdfPTable incomeSummaryTable = createTable(2, 1, 1);
        addTableHeader(incomeSummaryTable, "Category", "Amount");
        for (Map.Entry<Category, Double> entry : customSummary.getIncomePerCategory().entrySet()) {
            addCell(incomeSummaryTable, entry.getKey().getDisplayName());
            addCell(incomeSummaryTable, String.format("%.2f", entry.getValue()));
        }
        document.add(incomeSummaryTable);
    }
}
