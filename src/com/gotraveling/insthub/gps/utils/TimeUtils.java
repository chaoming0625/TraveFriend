package com.gotraveling.insthub.gps.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtils
 * 
 * @author ChaoMing
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");

    private TimeUtils() {
        throw new AssertionError();
    }

    public static String getTime(Date d,String fmt){
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.format(d);
    }
    public static String getTime(long timeInMillis, String fmt){
        SimpleDateFormat f = new SimpleDateFormat(fmt);
        return f.format(new Date(timeInMillis));
    }
    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     *  convert time to hour:minute:second
     * @param l
     * @return
     */
    public static String TimeToHms(long l) {
        if (l == 0L)  return "";
        long l1 = l / 1000L;
        String s;
        if (l1 >= 0L) {
            s = "+";
        } else {
            s = "-";
            l1 = -l1;
        }
        if (l1 >= 3600L) {
            s += String.valueOf(l1 / 3600L)+":";
            l1 %= 3600L;
        }
        if (l1 >= 60L) {
            s += String.valueOf(l1 / 60L) + ":";
            l1 %= 60L;
        }
        if (l1 > 0L) {
            s += String.valueOf(l1);
        }
        return s;
    }
    public static long getDays(Date d){
        long l1 = d.getTime() / 1000L;
        return l1 / 0x15180L;
    }
    public static long getDays(long d){
        long l1 = d / 1000L;
        return l1 / 0x15180L;
    }
    /**
     * convert time to days:hour:minute:second
     * @param l
     * @return
     */
    public static String TimeToDhm(long l) {
        if (l == 0L)           return "";

        StringBuilder s = new StringBuilder("");
        long l1 = l / 1000L;
        if (l1 >= 0x15180L) {
            s = s.append(l1 / 0x15180L).append("d");
            l1 %= 0x15180L;
        }
        if (l1 >= 3600L) {
            s = s.append(l1 / 3600L).append("h");
            l1 %= 3600L;
        }
        if (l1 >= 60L) {
            s = s.append(l1 / 60L).append("m");
            l1 %= 60L;
        }
        if (l1 >= 0L) {
            s = s.append(l1).append("s");
        }
        return s.toString();
    }
}
