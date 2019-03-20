package com.iaruchkin.deepbreath.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

//import static android.text.format.DateUtils.FORMAT_ABBREV_MONTH;
//import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;
//import static android.text.format.DateUtils.FORMAT_ABBREV_TIME;
//import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
//import static android.text.format.DateUtils.MINUTE_IN_MILLIS;
//import static android.text.format.DateUtils.WEEK_IN_MILLIS;

public final class StringUtils {

    public static String formatDate(@NonNull long epoch, String DATE_FORMAT){
        Date date = new Date(epoch*1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }
//
//    public static CharSequence formatEpoch(@NonNull Context context, @NonNull long epoch) {
//        int flags = FORMAT_ABBREV_RELATIVE |
//                FORMAT_SHOW_DATE | FORMAT_ABBREV_TIME | FORMAT_ABBREV_MONTH;
//        return DateUtils.getRelativeDateTimeString(context, new Date(epoch*1000).getTime(),
//                MINUTE_IN_MILLIS, WEEK_IN_MILLIS, flags);
//    }
//
//    public static CharSequence formatDate(@NonNull Context context, @NonNull Date date) {
//        int flags = FORMAT_ABBREV_RELATIVE |
//                FORMAT_SHOW_DATE | FORMAT_ABBREV_TIME | FORMAT_ABBREV_MONTH;
//
//        return DateUtils.getRelativeDateTimeString(context, date.getTime(),
//                MINUTE_IN_MILLIS, WEEK_IN_MILLIS, flags);
//    }

    private StringUtils() {
        throw new AssertionError("No instances");
    }
}
