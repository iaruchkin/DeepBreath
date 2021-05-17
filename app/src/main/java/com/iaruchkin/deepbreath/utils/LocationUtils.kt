package com.iaruchkin.deepbreath.utils

import android.content.Context
import android.location.Location
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.common.AppPreferences

class LocationUtils {

    companion object {

        fun locationIsValid(latitude: Double, lontitude: Double, context: Context?): Boolean {
            val location = Location("Station location")
            location.latitude = latitude
            location.longitude = lontitude
            val distance = location.distanceTo(PreferencesHelper.getLocation(context))
            return distance < AppPreferences.setDistance(App.INSTANCE.applicationContext) * 1000
        }

        fun locationDistance(latitude: Double, lontitude: Double, context: Context?): Float {
            val location = Location("Station location")
            location.latitude = latitude
            location.longitude = lontitude
            return location.distanceTo(PreferencesHelper.getLocation(context))
        }
    }
}