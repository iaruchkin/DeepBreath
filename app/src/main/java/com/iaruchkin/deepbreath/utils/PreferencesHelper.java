package com.iaruchkin.deepbreath.utils;

import android.content.Context;
import android.location.Location;

import com.iaruchkin.deepbreath.common.AppPreferences;

import java.util.Locale;

import static com.iaruchkin.deepbreath.common.AppPreferences.getLocationCoordinates;

public final class PreferencesHelper {


    public static Location getLocation(Context context) { //todo сделать нормально
        Location location = new Location("DEFAULT_LOCATION");

        location.setLatitude(getLocationCoordinates(context)[0]);
        location.setLongitude(getLocationCoordinates(context)[1]);

        return location;
    }

    public static String getAqiParameter(Context context){
        if(AppPreferences.isLocationLatLonAvailable(context)) {
            return String.format(Locale.ENGLISH, "geo:%s;%s", PreferencesHelper.getLocation(context).getLatitude()
                    , PreferencesHelper.getLocation(context).getLongitude());
        }
        return "here";
    }

    public static String getWeatherParameter(Context context){
        if(AppPreferences.isLocationLatLonAvailable(context)) {
            return String.format(Locale.ENGLISH, "%s,%s", PreferencesHelper.getLocation(context).getLatitude()
                    , PreferencesHelper.getLocation(context).getLongitude());
        }
        return "auto:ip";
    }

}
