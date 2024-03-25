package com.link.Assessment.components;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.link.Assessment.model.Merchants;
import com.link.Assessment.model.Transactions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReceiptGenerator {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static ByteArrayInputStream generatePDFReport(List<Transactions> datalist, Merchants merchants,String paymentLink) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Style style = new Style();
            style.setBackgroundColor(Color.convertRgbToCmyk(new DeviceRgb(74, 20, 140)));

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            //////////params
            PdfFont f2 = PdfFontFactory.createFont(FontConstants.HELVETICA);
            PdfFont f1 = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

            document.setMargins(10, 10, 5, 10);
            document.add(createHeader());
            document.add(createHeader2(merchants));
            document.add(createHeader4(datalist.get(0)));
            document.add(createHeader3());
            createContent(document, datalist, merchants,paymentLink);
            document.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Transactions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static Paragraph buildBody(Transactions rs) {
        Paragraph p = new Paragraph();
        p.setWidthPercent(100);
        p.setFontSize(12);
        Paragraph p1 = getPage(45).setPaddingRight(2).setTextAlignment(TextAlignment.LEFT).setBorderLeft(new SolidBorder(Color.LIGHT_GRAY, 1));
        p1.add(rs.getProductName());
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p.add(p1);
        p1.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));
        p1 = getPage(25).setPaddingRight(2).setTextAlignment(TextAlignment.LEFT).setBorderLeft(new SolidBorder(Color.LIGHT_GRAY, 1));
        p1.add((rs.getQuantity() + ""));
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p.add(p1);
        p1.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));

        p1 = getPage(30).setPaddingRight(2).setTextAlignment(TextAlignment.LEFT).setBorderLeft(new SolidBorder(Color.LIGHT_GRAY, 1));
        p1.add(df.format(rs.getTotalAmount()) + "");
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p.add(p1);
        p1.setBorder(new SolidBorder(Color.LIGHT_GRAY, 1));

        p.setMarginTop(-13);
        p.setPadding(0);
        return p;
    }


    public static Paragraph createHeader3() {

        Paragraph p = new Paragraph();
        p.setWidthPercent(100);
        p.setBackgroundColor(Color.convertRgbToCmyk(new DeviceRgb(225, 225, 225)));
        Paragraph p1 = getPage(45).setFontColor(Color.BLACK).setTextAlignment(TextAlignment.CENTER);
        p1.add("Product");
        p1.setFontSize(12);
        p1.setTextAlignment(TextAlignment.LEFT);
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p.add(p1);
        p1 = getPage(25).setFontColor(Color.BLACK).setTextAlignment(TextAlignment.CENTER);
        p1.add("Quantity");
        p1.setFontSize(12);
        //p1.setBold();
        p1.setTextAlignment(TextAlignment.LEFT);
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p.add(p1);

        p1 = getPage(30);
        p1.setFontSize(12);
        p1.add("Amount").setFontColor(Color.BLACK).setTextAlignment(TextAlignment.CENTER);
        //p1.setBold();
        p1.setTextAlignment(TextAlignment.LEFT);
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p.add(p1);

        p.setBorderBottom(new SolidBorder(Color.convertRgbToCmyk(new DeviceRgb(225, 225, 225)), 3));
        p.setMarginBottom(10);
        p.setPaddingBottom(-8);

        return p;
    }

    public static Paragraph createHeader() {
        Paragraph p = new Paragraph();
        p.setWidthPercent(100);
        Paragraph p1 = getPage(100);
        p1 = getPage(30);
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p1.setTextAlignment(TextAlignment.CENTER);
        p1.setMarginTop(5);
        p.add(p1);
        p.setMarginTop(-20);
        return p;
    }

    public static void createContent(Document d, List<Transactions> list, Merchants merchants,String paymentLink) {
        Double totalAmount =0.0;
        createHeader2(merchants);
        for (Transactions rs : list) {
            totalAmount+= rs.getTotalAmount();
            d.add(buildBody(rs));
        }
        // Define the color and underline properties for the payment link
        Color linkColor = new DeviceRgb(0, 0, 255); // Blue color
        float linkFontSize = 12;
        boolean isUnderlined = true;

        Paragraph p1 = getPage(100);
        Paragraph pt2 = getPage(100);
        //pt2.setBorderBottom(new SolidBorder(Color.GRAY, 1));
        pt2.setFontSize(10);
        pt2.add(getPage(50).add("TOTAL:       " + df.format(totalAmount)));
        pt2.setBorderBottom(new SolidBorder(Color.GRAY, 1));
        pt2.setFontSize(12);
        pt2.setBold();
        pt2.add(getPage(50).add("Amount Paid:       " + df.format(totalAmount)));
       // pt2.setBorderBottom(new SolidBorder(Color.GRAY, 1));
        pt2.setFontSize(12);
        pt2.setFontSize(12);

// Create a Text element for the label "Payment Link: "
        Text label = new Text("Payment Link: ").setFontColor(Color.BLACK).setFontSize(linkFontSize);

// Create a Text element for the payment link URL with color and underline
        Text paymentLinkText = new Text(paymentLink).setFontColor(linkColor).setFontSize(linkFontSize);
        if (isUnderlined) {
            paymentLinkText = paymentLinkText.setUnderline();
        }

// Add the label and payment link Text objects to the paragraph
        pt2.add(label);
        pt2.add(paymentLinkText);

        p1.add(pt2);



        d.add(p1);

    }

    public static Paragraph getPage(float width) {
        Paragraph p = new Paragraph();
        p.setWidthPercent(width);
        return p;

    }


    public static Paragraph createHeader2(Merchants merchants) {
        Paragraph p = new Paragraph();
        p.setWidthPercent(100);
        Paragraph p1 = getPage(100);
        p1.add(merchants.getCompanyName()+ "\n");
        p1.add(merchants.getCompanyEmail()+ "\n");
        p1.add(merchants.getCompanyMobile());
        p1.setFontSize(16);
        p1.setBold();
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p1.setTextAlignment(TextAlignment.CENTER);
        p.add(p1);
        p.setMarginTop(-10);
        return p;
    }

    public static Paragraph createHeader4(Transactions transactions) {
        LocalDateTime dateCreated = transactions.getDateCreated();

// Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

// Format the date to display only hour and minute
        String formattedDate = dateCreated.format(formatter);
        Paragraph p = new Paragraph();
        p.setWidthPercent(100);
        Paragraph p1 = getPage(100);
        p1.add("Date:         " + (formattedDate) + "\n");
        p1.add("Invoice No:  " + transactions.getInvoiceNo() + "\n");
        p1.add("Customer:    " + transactions.getCustomerName() + "\n");
        p1.setFontSize(12);
        p1.setBold();
        p1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        p1.setTextAlignment(TextAlignment.LEFT);
        p.add(p1);
        p.setMarginTop(-10);
        return p;
    }

}
