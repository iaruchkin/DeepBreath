package com.iaruchkin.deepbreath.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;

public final class StringUtils {

    public static String formatDate(@NonNull long epoch, String DATE_FORMAT){
        Date date = new Date(epoch*1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }

    public static String formatDateAqi(@NonNull long epoch, String DATE_FORMAT){
        Date date = new Date((epoch*1000 - TimeZone.getDefault().getRawOffset()));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }

    public static String formatTime(@NonNull String dateStr, String DATE_FORMAT){

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat date24Format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        try {
            return date24Format.format(date12Format.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    private StringUtils() {
        throw new AssertionError("No instances");
    }
}
