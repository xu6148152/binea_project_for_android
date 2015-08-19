package com.example.android.utils;

import java.util.GregorianCalendar;

/**
 * Created by xubinggui on 8/19/15.
 */
public class DateUtil {
    public static long getCurrentTimeStamps(){
        GregorianCalendar calendar = new GregorianCalendar(2015, 7, 18);
        return (System.currentTimeMillis() - calendar.getTimeInMillis()) / 1000;
    }
}
