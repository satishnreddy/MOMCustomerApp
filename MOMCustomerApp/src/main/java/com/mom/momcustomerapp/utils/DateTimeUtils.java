package com.mom.momcustomerapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author shailesh.lobo
 * @version -
 * @link -
 * @created - 10-04-2020 00:37
 * @modified by -
 * @updated on -
 * @since -
 */
public class DateTimeUtils {

    private static final String APP_DATE_TIME_FORMAT = "d MMMM yyyy h:mm aaa";
    private static final String SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String APP_DATE_TIME_FORMAT_ONLY_DT = "d MMMM yyyy";
    private static final String SERVER_DATE_TIME_FORMAT_OLD = "dd-MM-yyyy";//31-08-2019

    public static String convertDtTimeInAppFormat(String inputTime){
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault());
            Date parse = inputFormat.parse(inputTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat(APP_DATE_TIME_FORMAT, Locale.getDefault()); //3 APRIL 2020 3:30 PM
            String mdy = outputFormat.format(parse);
            return mdy.toUpperCase();
        } catch (ParseException exp) {
            exp.printStackTrace();
        }
        return inputTime;
    }

    public static String convertDtWithOldFormat(String inputTime){
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(SERVER_DATE_TIME_FORMAT_OLD, Locale.getDefault());
            Date parse = inputFormat.parse(inputTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat(APP_DATE_TIME_FORMAT_ONLY_DT, Locale.getDefault());//3 APRIL 2020 3:30 PM
            String mdy = outputFormat.format(parse);
            return mdy.toUpperCase();
        } catch (ParseException exp) {
            exp.printStackTrace();
        }
        return inputTime;
    }

    public static String getTodaysDtTime(){
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()); //3 APRIL 2020 3:30 PM
        String mdy = outputFormat.format(new Date());
        return mdy.toUpperCase();
    }
}
