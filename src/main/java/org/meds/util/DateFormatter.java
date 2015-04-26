package org.meds.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateFormatter {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm d/MM/yyyy");

    public static String format(Date date) {
        return dateFormat.format(date);
    }

    public static String format(long time) {
        return format(new Date(time));
    }
}
