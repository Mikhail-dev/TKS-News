package com.mikhaildev.tsknews.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {

    public static final int  SECOND = 1000;
    public static final int  MINUTE = 60 * SECOND;
    public static final int  HOUR   = 60 * MINUTE;
    public static final long DAY    = 24 * HOUR;
    public static final long MONTH  = 30 * DAY;

    private static SimpleDateFormat shortFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static String formatShortDate(Date value) {
        String currDate = shortFormat.format(value);
        return currDate.toString();
    }
}
