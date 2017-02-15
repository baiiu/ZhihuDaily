package com.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * author: baiiu
 * date: on 16/4/7 20:36
 * description:
 */
public class DateClass {
    public static void main(String[] args) throws ParseException {
        getYesterDayDate("20160102");
        getYesterDayDate("20160101");
        getYesterDayDate("20151231");
    }

    public static void getYesterDayDate(String date) throws ParseException {

        //解析指定Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date parseDate = simpleDateFormat.parse(date);

        //设置Calendar
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(parseDate);

        //回滚
        backOne(calendar);
        //backTwo(calendar);

        //转换格式
        String format = simpleDateFormat.format(calendar.getTime());

        System.out.println(format);
    }

    private static void backOne(Calendar calendar) {
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
    }

    private static void backTwo(Calendar calendar) {
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 1);
    }
}
