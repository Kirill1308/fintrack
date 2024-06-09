package com.popov.fintrack.report.formatter.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.popov.fintrack.user.model.member.Member;
import com.popov.fintrack.wallet.model.Wallet;

public abstract class BasePdfFormatter {

    protected static final BaseColor TABLE_HEADER_BG_COLOR = new BaseColor(149, 104, 171); // #9568ab
    protected static final BaseColor TABLE_BORDER_COLOR = new BaseColor(220, 212, 232); // #dcd4e8
    protected static final BaseColor TEXT_COLOR = new BaseColor(86, 74, 108); // #564a6c

    protected static final Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, TABLE_HEADER_BG_COLOR);
    protected static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    protected static final Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, TEXT_COLOR);

    protected void addWalletInfo(Document document, Wallet wallet) throws DocumentException {
        PdfPTable walletInfoTable = new PdfPTable(2);
        walletInfoTable.setWidthPercentage(100);
        walletInfoTable.setSpacingBefore(10);
        walletInfoTable.setSpacingAfter(10);
        walletInfoTable.setWidths(new float[]{1, 2});
        walletInfoTable.getDefaultCell().setBorderColor(TABLE_BORDER_COLOR);

        addStyledCell(walletInfoTable, "Wallet Name:", true);
        addStyledCell(walletInfoTable, wallet.getName(), false);

        addStyledCell(walletInfoTable, "Currency:", true);
        addStyledCell(walletInfoTable, wallet.getCurrency(), false);

        addStyledCell(walletInfoTable, "Owner:", true);
        String ownerName = wallet.getOwner().getName();
        String ownerUsername = wallet.getOwner().getUsername();
        addStyledCell(walletInfoTable, ownerName + " (" + ownerUsername + ")", false);

        addMembers(walletInfoTable, wallet);

        document.add(walletInfoTable);
    }

    protected void addMembers(PdfPTable walletInfoTable, Wallet wallet) {
        if (wallet.getMembers() != null && !wallet.getMembers().isEmpty()) {
            PdfPCell membersHeaderCell = new PdfPCell(new Phrase("Members:", headerFont));
            membersHeaderCell.setBackgroundColor(TABLE_HEADER_BG_COLOR);
            membersHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            membersHeaderCell.setPadding(5);
            membersHeaderCell.setBorderColor(TABLE_BORDER_COLOR);
            membersHeaderCell.setColspan(2);
            walletInfoTable.addCell(membersHeaderCell);

            for (Member member : wallet.getMembers()) {
                addStyledCell(walletInfoTable, "-", true);
                String memberName = member.getUser().getName();
                String memberUsername = member.getUser().getUsername();
                addStyledCell(walletInfoTable, memberName + " (" + memberUsername + ")", false);
            }
        }
    }

    protected void addStyledCell(PdfPTable table, String text, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(text, isHeader ? headerFont : bodyFont));
        cell.setBackgroundColor(isHeader ? TABLE_HEADER_BG_COLOR : BaseColor.WHITE);
        cell.setHorizontalAlignment(isHeader ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
        cell.setPadding(5);
        cell.setBorderColor(TABLE_BORDER_COLOR);
        table.addCell(cell);
    }

    protected PdfPTable createTable(int numColumns, float... widths) throws DocumentException {
        PdfPTable table = new PdfPTable(numColumns);
        table.setWidthPercentage(100);
        table.setWidths(widths);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.getDefaultCell().setBorderColor(TABLE_BORDER_COLOR);
        return table;
    }

    protected void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(TABLE_HEADER_BG_COLOR);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            cell.setBorderColor(TABLE_BORDER_COLOR);
            table.addCell(cell);
        }
    }

    protected void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBorderColor(TABLE_BORDER_COLOR);
        table.addCell(cell);
    }

    protected Paragraph createStyledParagraph(String content) {
        Paragraph paragraph = new Paragraph(content, bodyFont);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        return paragraph;
    }

    protected void addTitle(Document document, String titleText) throws DocumentException {
        Paragraph title = createTitle(titleText);
        document.add(new Paragraph("\n"));
        document.add(title);
    }

    protected Paragraph createTitle(String titleText) {
        Paragraph title = new Paragraph(titleText, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        return title;
    }
}
