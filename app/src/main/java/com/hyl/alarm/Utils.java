package com.hyl.alarm;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static final String LOG_TAG = "Utils";
    static long getTimeInMillis(String date)
    {
        Calendar a = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date dt1 = null;
        try {
            dt1 = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        a.setTime(dt1);
        long time = a.getTimeInMillis();
        Log.e(LOG_TAG,date + " getTimeInMillis = " + String.valueOf(time));
        return time;

    }
}
