package com.popov.fintrack.report.formatter;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PageNumberEvent extends PdfPageEventHelper {
    private static final Font pageNumberFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, new BaseColor(86, 74, 108));

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(String.valueOf(writer.getPageNumber()), pageNumberFont),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
    }
}

