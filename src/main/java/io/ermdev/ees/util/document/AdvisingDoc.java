package io.ermdev.ees.util.document;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.Course;
import io.ermdev.ees.model.Student;
import io.ermdev.ees.util.ResourceHelper;
import io.ermdev.ees.model.recursive.Subject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdvisingDoc {

    private final String path = "pdf/";
    private final Student STUDENT = new Student();
    private final List<Subject> SUBJECT_LIST = new ArrayList<>();

    static final Font small = new Font(Font.FontFamily.HELVETICA, 9);
    static final Font regular1 = new Font(Font.FontFamily.HELVETICA, 10);
    static final Font regular = new Font(Font.FontFamily.HELVETICA, 11);
    static final Font small_bold = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
    static final Font bold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
    static final Font bbold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    static final Font REGULAR_TIMES_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    static final Font BOLD_TIMES_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

    private CreationListener creationListener;

    public void setStudent(Student student) {
        STUDENT.setFirstName(student.getFirstName());
        STUDENT.setLastName(student.getLastName());
        STUDENT.setMiddleName(student.getMiddleName());
        STUDENT.setStudentNumber(student.getStudentNumber());
        STUDENT.setCourseId(student.getCourseId());
        STUDENT.setContactNumber(student.getContactNumber());
    }

    public void setCreationListener(CreationListener creationListener) {
        this.creationListener = creationListener;
    }

    public void addSubject(Subject subject) {
        SUBJECT_LIST.add(subject);
    }

    public void addRawSubject(io.ermdev.ees.model.Subject subject) {
        SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit(),
                subject.getUnitDisplay()));
    }

    public void addSubject(List<Subject> subjectList) {
        SUBJECT_LIST.addAll(subjectList);
    }

    public void addRawSubject(List<io.ermdev.ees.model.Subject> subjectList) {
        for(io.ermdev.ees.model.Subject subject : subjectList) {
            SUBJECT_LIST.add(new Subject(subject.getId(), subject.getName(), subject.getDesc(), subject.getUnit(),
                    subject.getUnitDisplay()));
        }
    }

    public void clearList() {
        SUBJECT_LIST.clear();
    }

    private PdfPTable getHeader() throws DocumentException, IOException {
        PdfPTable tblHeader = new PdfPTable(3);
        tblHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        tblHeader.getDefaultCell().setBorderWidth(0);
        tblHeader.setWidthPercentage(100);
        tblHeader.setWidths(new float[]{12, 76, 12});
        tblHeader.getDefaultCell().setUseAscender(true);
        tblHeader.getDefaultCell().setUseDescender(true);

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(new Paragraph("Republic of the Philippine", small));
        paragraph.add(new Paragraph("COMMISION ON HIGHER EDUCATION", small_bold));
        paragraph.add(new Paragraph("DON HONORIO VENTURA TECHNOLOGICAL STATE UNIVERSITY", small_bold));
        paragraph.add(new Paragraph("Bacolor, Pampanga", small));
        paragraph.add(new Paragraph("COLLEGE OF COMPUTING STUDIES", bold));
        paragraph.add(new Paragraph("ADVISING SLIP", bold));
        paragraph.add("\n");

        Image imgLeft = Image.getInstance(ResourceHelper.resourceWithBasePath("image/logodhvtsu.jpg"));
        Image imgRight = Image.getInstance(ResourceHelper.resourceWithBasePath("image/ccslogo.png"));

        PdfPCell txCenter = new PdfPCell();
        txCenter.addElement(paragraph);
        txCenter.setHorizontalAlignment(Element.ALIGN_CENTER);
        txCenter.setBorderWidth(0);
        txCenter.setBorder(0);

        tblHeader.addCell(imgLeft);
        tblHeader.addCell(txCenter);
        tblHeader.addCell(imgRight);

        return tblHeader;
    }

    private Paragraph getDate() {
        Calendar calendar = Calendar.getInstance();
        Paragraph date = new Paragraph("DATE :", regular1);
        date.setAlignment(Element.ALIGN_RIGHT);
        date.add(format(String.format(Locale.ENGLISH, "%d/%d/%d", calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR))));
        date.add("\n \n");
        return date;
    }

    private Paragraph getTotalUnit() {
        int totalUnit = 0;
        for(Subject subject : SUBJECT_LIST)
            totalUnit += subject.getUnit();

        Paragraph pUnit = new Paragraph("TOTAL UNITS: ", regular1);
        pUnit.add(format(totalUnit + "", (totalUnit + "").length() + 2));
        pUnit.add("\n \n");
        pUnit.setAlignment(Element.ALIGN_CENTER);
        return pUnit;
    }

    private PdfPTable getBasicInfo() throws DocumentException, IOException {
        PdfPTable tblBasicInfo = new PdfPTable(2);
        tblBasicInfo.getDefaultCell().setBorderWidth(1);
        tblBasicInfo.setWidthPercentage(100);
        tblBasicInfo.setWidths(new float[]{35, 65});
        tblBasicInfo.getDefaultCell().setUseAscender(true);
        tblBasicInfo.getDefaultCell().setUseDescender(true);
        tblBasicInfo.setHorizontalAlignment(Element.ALIGN_RIGHT);

        Course course = DbFactory.courseFactory().getCourseById(STUDENT.getCourseId());
        if(course == null)
            course = new Course();

        Paragraph pSN = new Paragraph("STUDENT NO. :", regular1);
        pSN.add(format(STUDENT.getStudentNumber() + ""));

        Paragraph pStatus = new Paragraph("1st/2nd/SUMMER :", regular1);
        pStatus.add(format(""));
        pStatus.setAlignment(Element.ALIGN_RIGHT);

        Paragraph pCourse = new Paragraph("COURSE :       ", regular1);
        pCourse.add(format(course.getName()));

        Paragraph pMajor = new Paragraph("MAJOR :", regular1);
        pMajor.add(format(""));
        pMajor.setAlignment(Element.ALIGN_RIGHT);

        PdfPCell cellSN = new PdfPCell();
        cellSN.addElement(pSN);
        cellSN.setBorderWidth(0);
        cellSN.setBorder(0);
        cellSN.setExtraParagraphSpace(pSN.getExtraParagraphSpace());

        PdfPCell cellStatus = new PdfPCell();
        cellStatus.addElement(pStatus);
        cellStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellStatus.setBorderWidth(0);
        cellStatus.setBorder(0);

        PdfPCell cellCourse = new PdfPCell();
        cellCourse.addElement(pCourse);
        cellCourse.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellCourse.setBorderWidth(0);
        cellCourse.setBorder(0);

        PdfPCell cellMajor = new PdfPCell();
        cellMajor.addElement(pMajor);
        cellMajor.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMajor.setBorderWidth(0);
        cellMajor.setBorder(0);

        PdfPCell cellEmpty = new PdfPCell();
        cellEmpty.addElement(new Paragraph("\n"));
        cellEmpty.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellEmpty.setBorderWidth(0);
        cellEmpty.setBorder(0);

        tblBasicInfo.addCell(cellSN);
        tblBasicInfo.addCell(cellStatus);
        tblBasicInfo.addCell(cellCourse);
        tblBasicInfo.addCell(cellMajor);
        tblBasicInfo.addCell(cellEmpty);
        tblBasicInfo.addCell(cellEmpty);
        return tblBasicInfo;
    }

    private PdfPTable getTable() throws DocumentException, IOException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(102);
        table.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBorderWidth(0.5f);
        table.setWidths(new float[]{20, 52, 15, 15});
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);

        table.addCell(new CslCell(new Paragraph("CODE", BOLD_TIMES_ROMAN)).paddingLeft(3).paddingBottom(2).borderTop(1).borderLeft(1));
        table.addCell(new CslCell(new Paragraph("Subject Description", BOLD_TIMES_ROMAN)).paddingLeft(3).paddingBottom(2).borderTop(1));
        table.addCell(new CslCell(new Paragraph("UNIT", BOLD_TIMES_ROMAN)).paddingLeft(3).paddingBottom(2).borderTop(1));
        table.addCell(new CslCell(new Paragraph("YR & SEC", BOLD_TIMES_ROMAN)).paddingLeft(3).paddingBottom(2).borderTop(1).borderRight(1));

        final int size = SUBJECT_LIST.size();
        for (int i = 0; i < 14; i++) {
            if (size > i) {
                Subject subject = SUBJECT_LIST.get(i);
                table.addCell(new CslCell(new Paragraph(subject.getName(), regular1)).padding(3).paddingTop(2).paddingBottom(2.5f).borderLeft(1));
                table.addCell(new CslCell(new Paragraph(subject.getDesc(), regular1)).padding(3).paddingBottom(2.5f));
                table.addCell(new CslCell(new Paragraph(subject.getUnit() + "", regular1)).padding(3).paddingBottom(2.5f));
                table.addCell(new CslCell(new Paragraph("")).padding(3).paddingBottom(2.5f).borderRight(1));

            } else {
                table.addCell(new CslCell(new Paragraph("")).height(20).padding(3).paddingBottom(2.5f).borderLeft(1));
                table.addCell(new CslCell(new Paragraph("")).height(20).padding(3).paddingBottom(2.5f));
                table.addCell(new CslCell(new Paragraph("")).height(20).padding(3).paddingBottom(2.5f));
                table.addCell(new CslCell(new Paragraph("")).height(20).padding(3).paddingBottom(2.5f).borderRight(1));
            }
        }
        return table;
    }

    private PdfPTable getSignatureForm() throws DocumentException, IOException {
        PdfPTable tblSignature = new PdfPTable(4);
        tblSignature.setWidthPercentage(100);
        tblSignature.setWidths(new float[]{32, 18, 18, 32});
        tblSignature.getDefaultCell().setUseAscender(true);
        tblSignature.getDefaultCell().setUseDescender(true);

        Paragraph pDean = new Paragraph();
        pDean.setFont(regular1);
        pDean.add(format("JOEL D. CANLAS, MIT,MBA", 32));
        pDean.setAlignment(Element.ALIGN_CENTER);

        Paragraph pStudent = new Paragraph();
        pStudent.setFont(regular1);
        pStudent.add(format(String.format("%s %s. %s", STUDENT.getFirstName(),
                (STUDENT.getMiddleName() != null ? STUDENT.getMiddleName().substring(0, 1) : ""),
                STUDENT.getLastName(), 32)));
        pStudent.setAlignment(Element.ALIGN_CENTER);

        Paragraph pDeanLabel = new Paragraph();
        pDeanLabel.setFont(small);
        pDeanLabel.add("Adviser/Dean");
        pDeanLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph pStudentLabel = new Paragraph();
        pStudentLabel.setFont(small);
        pStudentLabel.add("(Printed Name & Signature)");
        pStudentLabel.setAlignment(Element.ALIGN_CENTER);

        tblSignature.addCell(new CslCell(pDean).border(0));
        tblSignature.addCell(new CslCell(new Paragraph("")).border(0));
        tblSignature.addCell(new CslCell(new Paragraph("")).border(0));
        tblSignature.addCell(new CslCell(pStudent).border(0));

        tblSignature.addCell(new CslCell(pDeanLabel).border(0).vertical(Element.ALIGN_TOP));
        tblSignature.addCell(new CslCell(new Paragraph("")).border(0));
        tblSignature.addCell(new CslCell(new Paragraph("")).border(0));
        tblSignature.addCell(new CslCell(pStudentLabel).border(0).vertical(Element.ALIGN_TOP));

        return tblSignature;
    }

    private PdfPTable getStudentForm() throws DocumentException, IOException {
        PdfPTable tblSignature = new PdfPTable(3);
        tblSignature.setWidthPercentage(99);
        tblSignature.setWidths(new float[]{33, 33, 33});
        tblSignature.getDefaultCell().setUseAscender(true);
        tblSignature.getDefaultCell().setUseDescender(true);

        Paragraph pFName = new Paragraph();
        pFName.setFont(regular1);
        pFName.add(format(STUDENT.getFirstName(), 33));
        pFName.setAlignment(Element.ALIGN_CENTER);

        Paragraph pFNameLabel = new Paragraph();
        pFNameLabel.setFont(small);
        pFNameLabel.add("First Name");
        pFNameLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph pLName = new Paragraph();
        pLName.setFont(regular1);
        pLName.add(format(STUDENT.getLastName(), 33));
        pLName.setAlignment(Element.ALIGN_CENTER);

        Paragraph pLNameLabel = new Paragraph();
        pLNameLabel.setFont(small);
        pLNameLabel.add("Last Name");
        pLNameLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph pMName = new Paragraph();
        pMName.setFont(regular1);
        pMName.add(format(STUDENT.getMiddleName(), 33));
        pMName.setAlignment(Element.ALIGN_CENTER);

        Paragraph pMNameLabel = new Paragraph();
        pMNameLabel.setFont(small);
        pMNameLabel.add("Middle Name");
        pMNameLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph pAddress1 = new Paragraph();
        pAddress1.setFont(regular1);
        pAddress1.add(format("", 33));
        pAddress1.setAlignment(Element.ALIGN_CENTER);

        Paragraph pAddress1Label1 = new Paragraph();
        pAddress1Label1.setFont(small);
        pAddress1Label1.add("Number, Street");
        pAddress1Label1.setAlignment(Element.ALIGN_CENTER);

        Paragraph pAddress2 = new Paragraph();
        pAddress2.setFont(regular1);
        pAddress2.add(format("", 33));
        pAddress2.setAlignment(Element.ALIGN_CENTER);

        Paragraph pAddress1Label2 = new Paragraph();
        pAddress1Label2.setFont(small);
        pAddress1Label2.add("Barangay");
        pAddress1Label2.setAlignment(Element.ALIGN_CENTER);

        Paragraph pAddress3 = new Paragraph();
        pAddress3.setFont(regular1);
        pAddress3.add(format("", 33));
        pAddress3.setAlignment(Element.ALIGN_CENTER);

        Paragraph pAddress1Label3 = new Paragraph();
        pAddress1Label3.setFont(small);
        pAddress1Label3.add("Municipality");
        pAddress1Label3.setAlignment(Element.ALIGN_CENTER);

        Paragraph pProvince = new Paragraph();
        pProvince.setFont(regular1);
        pProvince.add(format("", 33));
        pProvince.setAlignment(Element.ALIGN_CENTER);

        Paragraph pProvinceLabel = new Paragraph();
        pProvinceLabel.setFont(small);
        pProvinceLabel.add("Province");
        pProvinceLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph pDR = new Paragraph();
        pDR.setFont(regular1);
        pDR.add(format("", 33));
        pDR.setAlignment(Element.ALIGN_CENTER);

        Paragraph pDRLabel = new Paragraph();
        pDRLabel.setFont(small);
        pDRLabel.add("District/Region");
        pDRLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph pContact = new Paragraph();
        pContact.setFont(regular1);
        pContact.add(format("+63 " + STUDENT.getContactNumber(), 33));
        pContact.setAlignment(Element.ALIGN_CENTER);

        Paragraph pContactLabel = new Paragraph();
        pContactLabel.setFont(small);
        pContactLabel.add("Contact Number");
        pContactLabel.setAlignment(Element.ALIGN_CENTER);


        tblSignature.addCell(new CslCell(pLName).border(0));
        tblSignature.addCell(new CslCell(pFName).border(0));
        tblSignature.addCell(new CslCell(pMName).border(0));

        tblSignature.addCell(new CslCell(pLNameLabel).border(0));
        tblSignature.addCell(new CslCell(pFNameLabel).border(0));
        tblSignature.addCell(new CslCell(pMNameLabel).border(0));

        tblSignature.addCell(new CslCell(pAddress1).border(0));
        tblSignature.addCell(new CslCell(pAddress2).border(0));
        tblSignature.addCell(new CslCell(pAddress3).border(0));

        tblSignature.addCell(new CslCell(pAddress1Label1).border(0));
        tblSignature.addCell(new CslCell(pAddress1Label2).border(0));
        tblSignature.addCell(new CslCell(pAddress1Label3).border(0));

        tblSignature.addCell(new CslCell(pProvince).border(0));
        tblSignature.addCell(new CslCell(pDR).border(0));
        tblSignature.addCell(new CslCell(pContact).border(0));

        tblSignature.addCell(new CslCell(pProvinceLabel).border(0));
        tblSignature.addCell(new CslCell(pDRLabel).border(0));
        tblSignature.addCell(new CslCell(pContactLabel).border(0));

        return tblSignature;
    }

    public void create() {
        Document document = new Document();
        FileOutputStream fos = null;
        try {
            fos=new FileOutputStream(new File(path.concat(STUDENT.getStudentNumber() + ".pdf")));
            fos.flush();

            final PdfWriter writer = PdfWriter.getInstance(document, fos);

            Paragraph pTitle = new Paragraph("__________________________________________________" +
                    "_________________________\n", bbold);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            pTitle.add("STUDENT'S PROFILE");

            Paragraph studentDetails = new Paragraph();
            studentDetails.setFont(regular1);

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

            document.open();
            document.add(getHeader());
            document.add(getDate());
            document.add(getBasicInfo());
            document.add(getTable());
            document.add(getTotalUnit());
            document.add(getSignatureForm());
            document.add(pTitle);
            document.add(getStudentForm());
            document.close();

            fos.flush();
            fos.close();

            writer.flush();
            writer.close();
        } catch (Exception e) {
            if(fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            System.out.println("Failed to create the file");
            if(creationListener != null)
                creationListener.onError();
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

        Chunk sigUnderline = new Chunk(result + ".");
        sigUnderline.setUnderline(0.1f, -2f);

        return sigUnderline;
    }

    public Chunk format(String name, int size) {
        int length = name.toCharArray().length;
        char c[] = name.toCharArray();
        String result = "";
        int counter = 0;
        for (int i = 0; i < size; i++) {
            if((size-length)/2 > i)
                result=result.concat(" ");
            else {
                if (length > counter) {
                    result=result.concat(String.valueOf(c[counter]));
                    counter++;
                } else
                    result=result.concat(" ");
            }
        }

        Chunk sigUnderline = new Chunk(result + ".");
        sigUnderline.setUnderline(0.1f, -2f);
        return sigUnderline;
    }

    @FunctionalInterface
    public interface CreationListener {
        void onError();
    }
}
