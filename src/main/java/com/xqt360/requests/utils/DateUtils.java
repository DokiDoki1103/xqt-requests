package com.xqt360.requests.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhangxiaoyuan
 * @date 2021/1/10 18:19
 */
public class DateUtils {

    public static List<String> getDaysBetween(int days){
        days = days - 1;
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        long startTime = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        long endTime = end.getTimeInMillis();
        long oneDay = 1000 * 60 * 60 * 24L;
        long time = startTime;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("MM-dd");
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }
    private static Date getDateAdd(int days){
        SimpleDateFormat sf = new SimpleDateFormat("MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    public static String formatDateToStr(Date date) {
        return formatDateToStr(date,"yyyy-MM-dd HH:mm:ss");

    }
    public static String formatDateToStr(Date date,String pattern) {
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return "2000-00-00 00:00:00";
        }
    }

    public static Date formatStrToDate(String str) {
        return formatStrToDate(str, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date formatStrToDate(String str, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (Exception ignored) {
        }
        return new Date();
    }
}
