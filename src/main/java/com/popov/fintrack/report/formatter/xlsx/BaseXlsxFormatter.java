package com.popov.fintrack.report.formatter.xlsx;

import com.popov.fintrack.wallet.membership.model.Member;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.model.Wallet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class BaseXlsxFormatter {
    // Sheet names
    protected static final String EXPENSES_PER_MONTH = "Expenses per Month";
    protected static final String INCOMES_PER_MONTH = "Incomes per Month";
    protected static final String EXPENSES_PER_CATEGORY = "Expenses per Category";
    protected static final String INCOMES_PER_CATEGORY = "Incomes per Category";
    protected static final String WALLET_INFO = "Wallet Info";

    // Styles
    private static final short TABLE_HEADER_BG_COLOR = IndexedColors.VIOLET.getIndex(); // #9568ab equivalent
    private static final short TEXT_COLOR = IndexedColors.GREY_80_PERCENT.getIndex(); // #564a6c equivalent

    protected void createWalletInfoSheet(Workbook workbook, Long walletId, WalletService walletService) {
        CellStyle headerStyle = getHeaderCellStyle(workbook);
        CellStyle dataStyle = getDataCellStyle(workbook);

        Wallet wallet = walletService.getWalletById(walletId);
        Sheet sheet = workbook.createSheet(WALLET_INFO);

        int rowIdx = 0;
        Row walletInfoHeaderRow = sheet.createRow(rowIdx++);
        createHeaderCell(walletInfoHeaderRow, 0, "Wallet Information", headerStyle);

        Row walletNameRow = sheet.createRow(rowIdx++);
        createDataCell(walletNameRow, 0, "Wallet Name: " + wallet.getName(), dataStyle);

        Row walletCurrencyRow = sheet.createRow(rowIdx++);
        createDataCell(walletCurrencyRow, 0, "Currency: " + wallet.getCurrency(), dataStyle);

        Row walletOwnerRow = sheet.createRow(rowIdx++);
        String ownerName = wallet.getOwner().getName();
        String ownerUsername = wallet.getOwner().getUsername();
        createDataCell(walletOwnerRow, 0, "Owner: " + ownerName + " (" + ownerUsername + ")", dataStyle);

        if (wallet.getMembers() != null) {
            Row walletMembersHeaderRow = sheet.createRow(rowIdx++);
            createHeaderCell(walletMembersHeaderRow, 0, "Members", headerStyle);
            for (Member member : wallet.getMembers()) {
                Row walletMemberRow = sheet.createRow(rowIdx++);
                String memberName = member.getUser().getName();
                String username = member.getUser().getUsername();
                createDataCell(walletMemberRow, 0, memberName + " (" + username + ")", dataStyle);
            }
        }

        autoSizeColumns(sheet, 1);
    }

    protected CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        style.setFillForegroundColor(TABLE_HEADER_BG_COLOR);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

    protected CellStyle getDataCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(TEXT_COLOR);
        style.setFont(font);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

    protected void createHeaderCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    protected void createDataCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String string) {
            cell.setCellValue(string);
        } else if (value instanceof Double aDouble) {
            cell.setCellValue(aDouble);
        } else if (value instanceof Integer anInt) {
            cell.setCellValue(anInt);
        }
        cell.setCellStyle(style);
    }

    protected void autoSizeColumns(Sheet sheet, int numberOfColumns) {
        for (int i = 0; i < numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}

