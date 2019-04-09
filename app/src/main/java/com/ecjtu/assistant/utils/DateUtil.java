package com.ecjtu.assistant.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author fedming
 */
public class DateUtil {

    public final static String fmtA = "yyyy-MM-dd";
    public final static String fmtB = "yyyy-MM-dd HH:mm:ss";
    public final static String fmtC = "yyyy-MM-dd HH:mm";
    public final static String fmtD = "yyyyMMddHHmmss";

    public static String nowtime(String pattern) {
        return fmt(System.currentTimeMillis(), pattern);
    }

    public static String formatToString(Date time) {

//		return formatToString(time, "yyyy-MM-hh HH:mm:ss");
        return formatToString(time, "yyyy-MM-dd");
    }

    public static String formatToString(Date time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 将时间戳根据 ftm 转为字符串
     *
     * @param timestamp long
     * @param pattern   String
     * @return String
     */
    public static String fmt(long timestamp, String pattern) {

        SimpleDateFormat simpledateformat = null;
        if (null == pattern) pattern = fmtB;

        try {
            simpledateformat = new SimpleDateFormat(pattern, Locale.CHINA);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "zero";
        }
        return simpledateformat.format(timestamp);
    }

    /**
     * 将时间戳根据 ftm 转为字符串
     *
     * @param timestamp Timestamp
     * @param pattern   String
     * @return String
     */
    public static String fmt(Timestamp timestamp, String pattern) {

        SimpleDateFormat simpledateformat = null;
        if (null == pattern) pattern = fmtB;

        try {
            simpledateformat = new SimpleDateFormat(pattern, Locale.CHINA);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "zero";
        }
        return simpledateformat.format(timestamp.getTime());
    }

    public static long t2l(Timestamp t) {
        return t.getTime();
    }

    public static Timestamp l2t(long l) {
        return new Timestamp(l);
    }

    /**
     * 将字符串根据 ftm 转为时间戳
     *
     * @param timestr String
     * @param pattern String
     * @return long
     */
    public static long timestampf(String timestr, String pattern) {

        SimpleDateFormat simpledateformat = null;
        Calendar cld = null;

        if (null == pattern) {

            if (timestr.indexOf(':') <= 0) {
                pattern = fmtA;
            } else {
                pattern = fmtB;
            }
        }

        try {
            simpledateformat = new SimpleDateFormat(pattern, Locale.CHINA);

            simpledateformat.parse(timestr);

            cld = simpledateformat.getCalendar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cld != null ? cld.getTimeInMillis() : 0;
    }

    public static int getDaynum(String timestr, String pattern) {
        long tl;

        if (null == timestr) {
            tl = timestampf(null, pattern);
        } else {
            tl = System.currentTimeMillis();
        }

        return (int) (tl / 86400000);
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp NowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static long getTodayTimeMillis() {
        return timestampf(nowtime(fmtA), null);
    }

    public static boolean isToday(String datetime) {

        long mtime = timestampf(datetime, fmtC);

        return mtime - getTodayTimeMillis() >= 0;
    }

    public static String formatDateTime(String datetime) {

        if (isToday(datetime)) {

            if (datetime.indexOf(" ") > 0) {

                String[] temp = datetime.split(" ");
                return "今日 " + temp[1];
            }

            return datetime;
        } else {
            long mtime = timestampf(datetime, fmtC);
            return fmt(mtime, "MM月dd日 HH:mm");
        }

    }

    /**
     * @param datetime 时间戳
     * @param pattern  格式化时间 格式
     * @return dateTime
     */
    public static String formatDateTime(long datetime, String pattern) {

        String dateTime = fmt(datetime, pattern);

        if (isToday(dateTime)) {

            if (dateTime.indexOf(" ") > 0) {

                String[] temp = dateTime.split(" ");
                return "今日 " + temp[1];
            }

            return dateTime;
        } else {
            long mtime = timestampf(dateTime, fmtC);
            return fmt(mtime, "MM-dd");
        }
    }

    /**
     * @param datetime 时间戳
     * @param pattern  格式化时间 格式
     * @param format   输出日期格式
     * @return dateTime
     */
    public static String formatDateTime(long datetime, String pattern, String format) {

        String dateTime = fmt(datetime, pattern);
        long mtime = timestampf(dateTime, fmtC);
        return fmt(mtime, format);
    }

}
