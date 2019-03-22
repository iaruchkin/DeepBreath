package com.iaruchkin.deepbreath.utils;

import android.util.Log;

import java.util.Locale;

public final class ConditionUtils {

    public static int getConditionCode(Integer code){

        switch (code){
            case 1000:
                return 0;
            case 1003:
                return 1;
            case 1006:
                return 2;
            case 1009:
                return 3;
            case 1030:
                return 4;
            case 1063:
                return 5;
            case 1066:
                return 6;
            case 1069:
                return 7;
            case 1072:
                return 8;
            case 1087:
                return 9;
            case 1114:
                return 10;
            case 1117:
                return 11;
            case 1135:
                return 12;
            case 1147:
                return 13;
            case 1150:
                return 14;
            case 1153:
                return 15;
            case 1168:
                return 16;
            case 1171:
                return 17;
            case 1180:
                return 18;
            case 1183:
                return 19;
            case 1186:
                return 20;
            case 1189:
                return 21;
            case 1192:
                return 22;
            case 1195:
                return 23;
            case 1198:
                return 24;
            case 1201:
                return 25;
            case 1204:
                return 26;
            case 1207:
                return 27;
            case 1210:
                return 28;
            case 1213:
                return 29;
            case 1216:
                return 30;
            case 1219:
                return 31;
            case 1222:
                return 32;
            case 1225:
                return 33;
            case 1237:
                return 34;
            case 1240:
                return 35;
            case 1243:
                return 36;
            case 1246:
                return 37;
            case 1249:
                return 38;
            case 1252:
                return 39;
            case 1255:
                return 40;
            case 1258:
                return 41;
            case 1261:
                return 42;
            case 1264:
                return 43;
            case 1273:
                return 44;
            case 1276:
                return 45;
            case 1279:
                return 46;
            case 1282:
                return 47;

            default:
                return 0;
        }
//todo implement all langs

    }

//    public static int getSmallArtResourceIdForWeatherCondition(int weatherId) {
//
//        /*
//         * Based on weather code data for Open Weather Map.
//         */
//        if (weatherId >= 200 && weatherId <= 232) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return R.drawable.ic_light_rain;
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return R.drawable.ic_rain;
//        } else if (weatherId == 511) {
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return R.drawable.ic_rain;
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return R.drawable.ic_fog;
//        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
//            return R.drawable.ic_storm;
//        } else if (weatherId == 800) {
//            return R.drawable.ic_clear;
//        } else if (weatherId == 801) {
//            return R.drawable.ic_light_clouds;
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return R.drawable.ic_cloudy;
//        } else if (weatherId >= 900 && weatherId <= 906) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 958 && weatherId <= 962) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 951 && weatherId <= 957) {
//            return R.drawable.ic_clear;
//        }
//
//        Log.e(LOG_TAG, "Unknown Weather: " + weatherId);
//        return R.drawable.ic_storm;
//    }

//    public static int getLargeArtResourceIdForWeatherCondition(int weatherId) {

//        if (weatherId >= 200 && weatherId <= 232) {
//            return R.drawable.art_storm;
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return R.drawable.art_light_rain;
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return R.drawable.art_rain;
//        } else if (weatherId == 511) {
//            return R.drawable.art_snow;
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return R.drawable.art_rain;
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return R.drawable.art_snow;
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return R.drawable.art_fog;
//        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
//            return R.drawable.art_storm;
//        } else if (weatherId == 800) {
//            return R.drawable.art_clear;
//        } else if (weatherId == 801) {
//            return R.drawable.art_light_clouds;
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return R.drawable.art_clouds;
//        } else if (weatherId >= 900 && weatherId <= 906) {
//            return R.drawable.art_storm;
//        } else if (weatherId >= 958 && weatherId <= 962) {
//            return R.drawable.art_storm;
//        } else if (weatherId >= 951 && weatherId <= 957) {
//            return R.drawable.art_clear;
//        }
//
//        Log.e(LOG_TAG, "Unknown Weather: " + weatherId);
//        return R.drawable.art_storm;
//    }

    private ConditionUtils() {
        throw new AssertionError("No instances");
    }
}
