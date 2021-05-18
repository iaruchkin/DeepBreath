package com.iaruchkin.deepbreath.utils

import android.content.Context
import android.location.Location
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.common.AppPreferences

class LocationUtils {

    companion object {

        fun locationIsValid(location1: Location, location2: Location): Boolean {
            val distance = location1.distanceTo(location2)
            return distance < AppPreferences.setDistance(App.INSTANCE.applicationContext) * 1000
        }

        fun locationIsValid(location: Location, context: Context?): Boolean {
            return locationIsValid(location, PreferencesHelper.getLocation(context))
        }

        fun locationDistance(location: Location?, context: Context?): Float {
            return location?.distanceTo(PreferencesHelper.getLocation(context)) ?: 0f
        }
    }
}