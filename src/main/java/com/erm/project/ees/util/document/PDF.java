package com.erm.project.ees.util.document;

import com.erm.project.ees.dao.impl.CourseDaoImpl;
import com.erm.project.ees.model.Student;
import com.erm.project.ees.model.recursive.Subject;
import com.erm.project.ees.util.ResourceHelper;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class PDF {

    private String name;
    private List<Subject> subjectList;
    private Student student;

    public PDF() {
        subjectList = new ArrayList<>();
        student = new Student();
    }

    public void write() {
        try {
            Document document = new Document();
            final String PATH = ResourceHelper.resourceWithBasePath("pdf/" + "haha.pdf").toString();
            File file = new File("sample.pdf");

            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            Font small = new Font(Font.FontFamily.HELVETICA, 9);
            Font regular1 = new Font(Font.FontFamily.HELVETICA, 10);
            Font regular = new Font(Font.FontFamily.HELVETICA, 11);
            Font small_bold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font bold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font bbold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);


            Paragraph line1 = new Paragraph("Republic of the Philippine", small);
            line1.setAlignment(Element.ALIGN_CENTER);
            Paragraph line2 = new Paragraph("COMMISION ON HIGHER EDUCATION", small_bold);
            line2.setAlignment(Element.ALIGN_CENTER);
            Paragraph line3 = new Paragraph("DON HONORIO VENTURA TECHNOLOGICAL STATE UNIVERSITY", small_bold);
            line3.setAlignment(Element.ALIGN_CENTER);
            Paragraph line4 = new Paragraph("Bacolor, Pampanga \n \n", small);
            line4.setAlignment(Element.ALIGN_CENTER);
            Paragraph line5 = new Paragraph("COLLEGE OF COMPUTING STUDIES", bold);
            line5.setAlignment(Element.ALIGN_CENTER);
            Paragraph line6 = new Paragraph("ADVISING SLIP", bold);
            line6.setAlignment(Element.ALIGN_CENTER);
            line6.add("\n \n \n");

            document.add(line1);
            document.add(line2);
            document.add(line3);
            document.add(line4);
            document.add(line5);
            document.add(line6);
            document.close();

            Desktop.getDesktop().open(new File(ResourceHelper.dir() + "/sample.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void writeAndClose() {

        Document document = new Document();
        try {
            File file = new File(ResourceHelper.dir() + "/sample3.pdf");
            System.out.println(file.exists());

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            Font small = new Font(Font.FontFamily.HELVETICA, 9);
            Font regular1 = new Font(Font.FontFamily.HELVETICA, 10);
            Font regular = new Font(Font.FontFamily.HELVETICA, 11);
            Font small_bold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
            Font bold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font bbold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);


            Paragraph line1 = new Paragraph("Republic of the Philippine", small);
            line1.setAlignment(Element.ALIGN_CENTER);
            Paragraph line2 = new Paragraph("COMMISION ON HIGHER EDUCATION", small_bold);
            line2.setAlignment(Element.ALIGN_CENTER);
            Paragraph line3 = new Paragraph("DON HONORIO VENTURA TECHNOLOGICAL STATE UNIVERSITY", small_bold);
            line3.setAlignment(Element.ALIGN_CENTER);
            Paragraph line4 = new Paragraph("Bacolor, Pampanga \n \n", small);
            line4.setAlignment(Element.ALIGN_CENTER);
            Paragraph line5 = new Paragraph("COLLEGE OF COMPUTING STUDIES", bold);
            line5.setAlignment(Element.ALIGN_CENTER);
            Paragraph line6 = new Paragraph("ADVISING SLIP", bold);
            line6.setAlignment(Element.ALIGN_CENTER);
            line6.add("\n \n \n");

            Paragraph date = new Paragraph("DATE :", regular1);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.add("________________");
            date.add("\n \n");

            Paragraph line7 = new Paragraph("STUDENT NO. :", regular1);
            //line7.add("Dog name");
            line7.setAlignment(Element.ALIGN_LEFT);
            line7.add(format(student.getStudentNumber() + ""));

            line7.add("                                                                  ");
            line7.add("1st/2nd/SUMMER :");
            line7.add(format(""));

            Paragraph line8 = new Paragraph("COURSE          :", regular1);
            line8.setAlignment(Element.ALIGN_LEFT);
            line8.add(format(new CourseDaoImpl().getCourseById(student.getCourseId()).getName()));
            line8.add("\t                                                               \t");
            line8.add("MAJOR                  :");
            line8.add("________________");
            line8.add("  \n ");

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(102);
            table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.setWidths(new float[]{20, 52, 15, 15});
            table.getDefaultCell().setUseAscender(true);
            table.getDefaultCell().setUseDescender(true);
            table.addCell("Codse");
            table.addCell("Subject Description");
            table.addCell("Unit");
            table.addCell("YR & SEC");


            final int size = subjectList.size();
            for (int i = 0; i < 14; i++) {
                if (size > i) {
                    Subject subject = subjectList.get(i);
                    PdfPCell codeCell = new PdfPCell();
                    codeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    codeCell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                    codeCell.addElement(new Paragraph(subject.getName()));
                    table.addCell(codeCell);
                    table.addCell(subject.getDesc());
                    table.addCell(subject.getUnit() + "");
                    table.addCell(" ");
                } else {
                    table.addCell(" ");
                    table.addCell(" ");
                    table.addCell(" ");
                    table.addCell(" ");
                }


            }

            Paragraph totalUnit = new Paragraph("TOTAL UNITS: ", regular1);
            totalUnit.add("____ \n \n");
            totalUnit.setAlignment(Element.ALIGN_CENTER);
            Paragraph deanAviser = new Paragraph("JOEL D. CANLAS, MIT,MBA ", regular1);
            deanAviser.setAlignment(Element.ALIGN_LEFT);
            deanAviser.add("\t                                                        "
                    + "                          \t");
            deanAviser.add("____________________________");
            deanAviser.add("             Adviser/Dean");
            deanAviser.add("\t                                                        "
                    + "                                               \t");
            deanAviser.add("(Printed Name & Signature)");

            Paragraph lineOnly = new Paragraph("__________________________________________________________________");
            lineOnly.setAlignment(Element.ALIGN_CENTER);
            Paragraph StudentProfile = new Paragraph("STUDENT'S PROFILE", bbold);
            StudentProfile.setAlignment(Element.ALIGN_CENTER);

            Paragraph studentDetails = new Paragraph("Name :", regular1);
            studentDetails.setAlignment(Element.ALIGN_LEFT);

            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \n ");
            studentDetails.add("Last");
            studentDetails.add("First");
            studentDetails.add("Middle \n");
            studentDetails.add("Address: ");

            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \n ");
            studentDetails.add("Number, Steet");
            studentDetails.add("Barangay");
            studentDetails.add("Minicipality \n ");

            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________\t \t ");
            studentDetails.add("______________________ \n ");
            studentDetails.add("Provice");
            studentDetails.add("District");
            studentDetails.add("Region");
            studentDetails.add("Contact Number \n");

            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \t \t");
            studentDetails.add("______________________ \t \t ");
            studentDetails.add("______________________ \n ");
            studentDetails.add("Birthdate");
            studentDetails.add("Citizenship");
            studentDetails.add("Region");
            studentDetails.add("Age");
            studentDetails.add("Gender");

            document.add(line1);
            document.add(line2);
            document.add(line3);
            document.add(line4);
            document.add(line5);
            document.add(line6);
            document.add(date);
            document.add(line7);
            document.add(line8);
            document.add(table);
            document.add(totalUnit);
            document.add(deanAviser);
            document.add(lineOnly);
            document.add(StudentProfile);
            document.add(studentDetails);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Chunk format(String name) {
        int length = name.toCharArray().length;
        char c[] = name.toCharArray();
        String result = "  ";
        for (int i = 0; i < 25; i++) {
            if (length > i) {
                result += c[i];
            } else
                result += " ";
        }

        Chunk sigUnderline = new Chunk(result);
        sigUnderline.setUnderline(0.1f, -2f);

        return sigUnderline;
    }
}
