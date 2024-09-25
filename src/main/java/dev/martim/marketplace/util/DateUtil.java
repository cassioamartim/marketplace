package dev.martim.marketplace.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDate(long millis, DateFormat format, boolean withHoursAndMinutes) {
        String date = new SimpleDateFormat("MM/dd/yy" + (withHoursAndMinutes ? (format.equals(DateFormat.BIG) ? " - " : " ") + "HH:mm" : ""))
                .format(new Date(millis));

        if (withHoursAndMinutes) {
            date = date.replace("-", "the");
        }

        return date;
    }

    public static String getSimpleDate(long millis) {
        return getDate(millis, DateFormat.SMALL, true);
    }

    public enum DateFormat {
        BIG, SMALL
    }
}
