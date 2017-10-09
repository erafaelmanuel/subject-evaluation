package com.erm.project.ees.util.document;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

public class CslCell extends PdfPCell {

    public CslCell(Paragraph paragraph) {
        addElement(paragraph);
        setVerticalAlignment(Element.ALIGN_MIDDLE);
        setHorizontalAlignment(Element.ALIGN_MIDDLE);
    }

    public CslCell vertical(int type) {
        setVerticalAlignment(type);
        return this;
    }

    public CslCell padding(float padding) {
        setPadding(padding);
        return this;
    }

    public CslCell paddingTop(float padding) {
        setPaddingTop(padding);
        return this;
    }

    public CslCell paddingRight(float padding) {
        setPaddingRight(padding);
        return this;
    }

    public CslCell paddingBottom(float padding) {
        setPaddingBottom(padding);
        return this;
    }

    public CslCell paddingLeft(float padding) {
        setPaddingLeft(padding);
        return this;
    }

    public CslCell border(float width) {
        setBorderWidth(width);
        return this;
    }

    public CslCell borderTop(float width) {
        setBorderWidthTop(width);
        return this;
    }

    public CslCell borderRight(float width) {
        setBorderWidthRight(width);
        return this;
    }

    public CslCell borderBottom(float width) {
        setBorderWidthBottom(width);
        return this;
    }

    public CslCell borderLeft(float width) {
        setBorderWidthLeft(width);
        return this;
    }

    public CslCell height(float height) {
        setFixedHeight(height);
        return this;
    }
}
