package com.iaruchkin.deepbreath.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.iaruchkin.deepbreath.R;

public final class AppPreferences {

    private static final String PREF_COORD_LAT = "coord_lat";
    private static final String PREF_COORD_LONG = "coord_long";
    private static final String FIRST = "first launch";

    public static void setLocationDetails(Context context, double lat, double lon) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putLong(PREF_COORD_LAT, Double.doubleToRawLongBits(lat));
        editor.putLong(PREF_COORD_LONG, Double.doubleToRawLongBits(lon));
        editor.apply();
    }

    public static void resetLocationCoordinates(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(PREF_COORD_LAT);
        editor.remove(PREF_COORD_LONG);
        editor.apply();
    }

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences
     * @return true if metric display should be used, false if imperial display should be used
     */
    public static boolean isMetric(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);

        boolean userPrefersMetric = false;
        if (metric.equals(preferredUnits)) {
            userPrefersMetric = true;
        }

        return userPrefersMetric;
    }

    public static int pressureUnits(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_press_key);
        String defaultUnits = context.getString(R.string.pref_press_rus);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);
        String imperial = context.getString(R.string.pref_press_imperial);
        String metric = context.getString(R.string.pref_press_metric);

        int userPrefers = 0;
        if (metric.equals(preferredUnits)) {
            userPrefers = 1;
        } else if(imperial.equals(preferredUnits)){
            userPrefers = 2;
        }
        return userPrefers;
    }

    public static int windUnits(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForUnits = context.getString(R.string.pref_wind_key);
        String defaultUnits = context.getString(R.string.pref_wind_rus);
        String preferredUnits = sp.getString(keyForUnits, defaultUnits);
        String imperial = context.getString(R.string.pref_wind_imperial);
        String metric = context.getString(R.string.pref_wind_metric);

        int userPrefers = 0;
        if (metric.equals(preferredUnits)) {
            userPrefers = 1;
        } else if(imperial.equals(preferredUnits)){
            userPrefers = 2;
        }
        return userPrefers;
    }

    /**
     * Returns the location coordinates associated with the location. Note that there is a
     * possibility that these coordinates may not be set, which results in (0,0) being returned.
     * Interestingly, (0,0) is in the middle of the ocean off the west coast of Africa.
     *
     * @param context used to access SharedPreferences
     * @return an array containing the two coordinate values for the user's preferred location
     */
    public static double[] getLocationCoordinates(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        double[] preferredCoordinates = new double[2];

        /*
         * This is a hack we have to resort to since you can't store doubles in SharedPreferences.
         *
         * Double.doubleToLongBits returns an integer corresponding to the bits of the given
         * IEEE 754 double precision value.
         *
         * Double.longBitsToDouble does the opposite, converting a long (that represents a double)
         * into the double itself.
         */
        preferredCoordinates[0] = Double
                 .longBitsToDouble(sp.getLong(PREF_COORD_LAT, Double.doubleToRawLongBits(0.0)));
        preferredCoordinates[1] = Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LONG, Double.doubleToRawLongBits(0.0)));

        return preferredCoordinates;
    }

    public static boolean isLocationLatLonAvailable(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        boolean spContainLatitude = sp.contains(PREF_COORD_LAT);
        boolean spContainLongitude = sp.contains(PREF_COORD_LONG);

        boolean spContainBothLatitudeAndLongitude = false;
        if (spContainLatitude && spContainLongitude) {
            spContainBothLatitudeAndLongitude = true;
        }

        return spContainBothLatitudeAndLongitude;
    }

    public static boolean areNotificationsEnabled(Context context) {
        /* Key for accessing the preference for showing notifications */
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);

        /*
         * If no preference has been chosen, we want to be able to determine
         * whether or not to show them. To do this, we reference a bool stored in bools.xml.
         */

        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        /* If a value is stored with the key, we extract it here. If not, use a default. */
        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

    public static boolean needToShowIntro(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean firstLaunch = sp.getBoolean(FIRST, true);

        //to show intro screen only at first launch
//        SharedPreferences.Editor editor = sp.edit();
//        if(firstLaunch){
//            editor.putBoolean(FIRST, false);
//            editor.apply();
//            return firstLaunch;
//        }

        return firstLaunch;
    }

    public static void introFinished(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(FIRST, false);
        editor.apply();
    }
}