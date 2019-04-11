package com.iaruchkin.deepbreath.utils;

import android.content.Context;
import android.location.Location;

public class LocationUtils {


    public static boolean locationIsValid(double latitude, double lontitude, Context context){

        Location location = new Location("Station location");
        location.setLatitude(latitude);
        location.setLongitude(lontitude);

        float distance = location.distanceTo(PreferencesHelper.getLocation(context));

        return distance < 200000;

    }
}
