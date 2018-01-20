package io.ermdev.ees.legacy.helper;

import java.util.Calendar;

public class DateHelper {

    private static Calendar calendar = Calendar.getInstance();

    public static String now() {
        return (calendar.get(Calendar.MONTH)+1)+ "/" +calendar.get(Calendar.DAY_OF_MONTH)+ "/" +calendar.get(Calendar.YEAR);
    }
}
