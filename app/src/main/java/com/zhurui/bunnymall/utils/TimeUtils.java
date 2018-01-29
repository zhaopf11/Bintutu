package com.zhurui.bunnymall.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * TimeUtils
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_MONTH_DAY = new SimpleDateFormat("MM月dd日");

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

    public static Date stringToDate(String dateStr) {

        Date date = null;
        try {
            date = DATE_FORMAT_DATE.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date) {
        return DATE_FORMAT_DATE.format(date);
    }

    /**
     * 将时间转为时间戳
     *
     * @param datestr
     * @return
     * @throws ParseException
     */
    public static long dateToLong(String datestr) throws ParseException {
        Date date = DEFAULT_DATE_FORMAT.parse(datestr);
        long ts = date.getTime();
        return ts;
    }

    /**
     * 获取当前时区的系统时间
     * @return
     * @throws Exception
     */
    public static long getCurrentTimeMillis() throws Exception {
        TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
        TimeZone.setDefault(tz);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置当地时间
        String timeStr = dateFormat.format(now);
        Date date = dateFormat.parse(timeStr);
        long time = date.getTime();
        return time;
    }

}
