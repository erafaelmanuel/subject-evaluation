package io.ermdev.ees.helper;

import io.ermdev.ees.model.v2.SecName;

public class SectionHelper {

    public static final int A = 1;
    public static final int B = 2;
    public static final int C = 3;
    public static final int D = 4;
    public static final int E = 5;
    public static final int F = 6;

    public static SecName getSectionName(int key) {
        switch (key) {
            case A: return SecName.A;
            case B: return SecName.B;
            case C: return SecName.C;
            case D: return SecName.D;
            case E: return SecName.E;
            case F: return SecName.F;
            default: return SecName.A;
        }
    }

    public static String format(int year) {
        switch (year) {
            case 1: return "1ST YEAR";
            case 2: return "2ND YEAR";
            case 3: return "3RD YEAR";
            case 4: return "4TH YEAR";
            case 5: return "5TH YEAR";
            default: return "n/a";
        }
    }
}
