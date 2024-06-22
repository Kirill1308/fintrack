package com.popov.fintrack.report.formatter.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.popov.fintrack.exception.ReportGenerationException;
import com.popov.fintrack.report.formatter.PageNumberEvent;
import com.popov.fintrack.summary.dto.MonthlySummary;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class MonthlyPdfFormatter extends BasePdfFormatter {

    private final WalletService walletService;

    public byte[] format(MonthlySummary monthlySummary) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
            writer.setPageEvent(new PageNumberEvent());
            document.open();

            addTitle(document, "Financial Report (" + monthlySummary.getMonth().name() + " " + monthlySummary.getYear() + ")");
            Wallet wallet = walletService.getWalletById(monthlySummary.getWalletId());
            addWalletInfo(document, wallet);

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
}
