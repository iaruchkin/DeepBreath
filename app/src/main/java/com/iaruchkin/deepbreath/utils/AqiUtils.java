
package com.iaruchkin.deepbreath.utils;

import android.util.Log;

import com.iaruchkin.deepbreath.R;


public final class AqiUtils {

    private static final String LOG_TAG = AqiUtils.class.getSimpleName();

    public static int getColor(int aqi) {

        if (aqi >= 0 && aqi <= 50) {
            return R.color.good;
        } else if (aqi > 50 && aqi <= 100) {
            return R.color.moderate;
        } else if (aqi > 100 && aqi <= 150) {
            return R.color.pre_unhealthy;
        } else if (aqi > 150 && aqi <= 200) {
            return R.color.unhealthy;
        } else if (aqi > 200 && aqi <= 300) {
            return R.color.very_unhealthy;
        } else if (aqi > 300) {
            return R.color.hazardous;
        }

        Log.e(LOG_TAG, "Unknown Weather: " + aqi);
        return R.color.moderate;
    }

    public static int getBackgroundColor(int aqi) {

        if (aqi >= 0 && aqi <= 50) {
            return R.color.good_transparent;
        } else if (aqi > 50 && aqi <= 100) {
            return R.color.moderate_transparent;
        } else if (aqi > 100 && aqi <= 150) {
            return R.color.pre_unhealthy_transparent;
        } else if (aqi > 150 && aqi <= 200) {
            return R.color.unhealthy_transparent;
        } else if (aqi > 200 && aqi <= 300) {
            return R.color.very_unhealthy_transparent;
        } else if (aqi > 300) {
            return R.color.hazardous_transparent;
        }

        Log.e(LOG_TAG, "Unknown Weather: " + aqi);
        return R.color.moderate_transparent;
    }

    public static int getPollutionLevel(int aqi) {

        if (aqi >= 0 && aqi <= 50) {
            return R.string.pollution_good;
        } else if (aqi > 50 && aqi <= 100) {
            return R.string.pollution_moderate;
        } else if (aqi > 100 && aqi <= 150) {
            return R.string.pollution_pre_unhealthy;
        } else if (aqi > 150 && aqi <= 200) {
            return R.string.pollution_unhealthy;
        } else if (aqi > 200 && aqi <= 300) {
            return R.string.pollution_very_unhealthy;
        } else if (aqi > 300) {
            return R.string.pollution_hazardous;
        }

        Log.e(LOG_TAG, "Unknown Weather: " + aqi);
        return R.string.pollution_moderate;
    }

    public static int getPollutionLevelFull(int aqi) {

        if (aqi >= 0 && aqi <= 50) {
            return R.string.good_full;
        } else if (aqi > 50 && aqi <= 100) {
            return R.string.moderate_full;
        } else if (aqi > 100 && aqi <= 150) {
            return R.string.pre_unhealthy_full;
        } else if (aqi > 150 && aqi <= 200) {
            return R.string.unhealthy_full;
        } else if (aqi > 200 && aqi <= 300) {
            return R.string.very_unhealty_full;
        } else if (aqi > 300) {
            return R.string.hazardous_full;
        }

        Log.e(LOG_TAG, "Unknown Weather: " + aqi);
        return R.string.pollution_moderate;
    }
}