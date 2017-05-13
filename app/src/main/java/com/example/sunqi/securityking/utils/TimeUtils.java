package com.example.sunqi.securityking.utils;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sshunsun on 2017/5/14.
 */
public class TimeUtils {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getTimeStr(long time) {
        Date date = new Date(time);
        return format.format(date);
    }

    private static boolean isToday(long time) {
        return DateUtils.isToday(time);
    }
}
