package com.saiyu.foreground.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 落叶
 */
public class TimeParseUtils {
    /**
     * @param args
     */
    public static void main(String[] args) {
        String s1 = "1991-01-01";
        StringToDate(s1);
        Date d = new Date();
        DateToString(d);
        Calendar calendar = DateToCalendar(d);
        CalendarToDate(calendar);
    }


    
    /**
     * 字符串转日期
     *
     * @param String
     */
    public static Date StringToDate(String String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date time = null;
        try {
            time = format.parse(String);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 字符串转毫秒
     *
     * @param String
     */
    public static long StringToLongMillis(String String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long millionSeconds = 0;//毫秒
        try {
            millionSeconds = format.parse(String).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.print("出异常了!!!");
        }
        return millionSeconds;
    }

    /**
     * 字符串转字符串
     *
     * @param string
     */
    public static String StringToString(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d HH:mm:ss");
        long millionSeconds = 0;//毫秒
        try {
            millionSeconds = format.parse(string).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.print("出异常了!!!");
        }
        string = LongToTeamTime(millionSeconds);
        return string;
    }


    /**
     * 毫秒转字符串
     *
     * @param timemillis
     */
    public static String LongMillisToString(long timemillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date(timemillis);
        String string = DateToString(date);
        return string;
    }


    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String string = format.format(date);
        return string;
    }

    /**
     * 日期转日历
     *
     * @param date
     * @return Calendar
     */
    public static Calendar DateToCalendar(Date date) {
        Calendar startdate = Calendar.getInstance();
        startdate.setTime(date);
        return startdate;
    }

    /**
     * 日历转日期
     *
     * @param calendar
     * @return Date
     */
    public static Date CalendarToDate(Calendar calendar) {
        Date date = calendar.getTime();
        return date;
    }


    /**
     * 毫秒转字符串
     *
     * @param timemillis
     */
    public static String LongToTeamTime(long timemillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(timemillis);
        String string = format.format(date);
        return string;
    }

    /**
     * 毫秒转字符串
     *
     * @param timemillis
     */
    public static String LongToTeamTime1(long timemillis) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d H:mm:ss");
        Date date = new Date(timemillis);
        String string = format.format(date);
        return string;
    }

    /**
     * 字符串转字符串
     *
     * @param String
     */
    public static long TeamTimeStringToString(String String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long millionSeconds = 0;//毫秒
        try {
            millionSeconds = format.parse(String).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.print("出异常了!!!");

        }
        return millionSeconds;
    }


    /**
     * 字符串转毫秒
     *
     * @param String
     */
    public static long TeamTimeStringToMills(String String) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long millionSeconds = 0;//毫秒
        try {
            millionSeconds = format.parse(String).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtils.print("出异常了!!!");

        }
        return millionSeconds;
    }

}