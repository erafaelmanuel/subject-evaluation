package com.erm.project.ees.util.document;

public class Test {

    public static void main(String args[]) {

        PDF pdf = new PDF();
        pdf.setName("test.pdf");
        pdf.writeAndClose();


    }
}
