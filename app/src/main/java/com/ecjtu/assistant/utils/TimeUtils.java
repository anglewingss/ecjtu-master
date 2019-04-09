package com.ecjtu.assistant.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 功能：
 * 作者： gaom
 * 日期 ：2018/4/4.
 * 时间：9:14.
 */

public class TimeUtils {
    /**
     * 获取系统时间戳
     * @return
     */
    public static long getCurTimeLong(){
        long time=System.currentTimeMillis();
        return time;
    }
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取两个日期之间的间隔天数
     * @return
     */
    public static String add30Days(String dateStr ) {
        Date endDate;
        endDate =  new Date(   TimeUtils.getStringToDate(dateStr,"yyyy-MM-dd"));

        Calendar   calendar   =   new GregorianCalendar();
        calendar.setTime(endDate);
        calendar.add(calendar.DATE,30);//把日期往后增加一天.整数往后推,负数往前移动
      Date  date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        return getDateToString(date.getTime(),"yyyy-MM-dd");
    }
    public static int getGapCount(String endS ) {
        Date startDate;
        Date endDate;
        endDate =  new Date(   TimeUtils.getStringToDate(endS,"yyyy-MM-dd"));
          startDate=  new Date(  getCurTimeLong() );
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }
}
