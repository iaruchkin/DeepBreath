package com.iaruchkin.deepbreath.utils;

import android.content.Context;
import android.location.Location;

import com.iaruchkin.deepbreath.common.AppPreferences;

import static com.iaruchkin.deepbreath.App.INSTANCE;

public class LocationUtils {


    public static boolean locationIsValid(double latitude, double lontitude, Context context){

        Location location = new Location("Station location");
        location.setLatitude(latitude);
        location.setLongitude(lontitude);

        float distance = location.distanceTo(PreferencesHelper.getLocation(context));

        return distance < AppPreferences.setDistance(INSTANCE.getApplicationContext())*1000;

    }
}
