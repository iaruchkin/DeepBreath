package com.iaruchkin.deepbreath.network.dtos.aqiAvDTO

import android.location.Location
import com.google.gson.annotations.SerializedName

class LocationAv {
    @SerializedName("type") var type: String? = null

    @SerializedName("coordinates") private var coordinates: List<Double>? = null

    fun getCoordinates(): Location {
        return Location("aqiAvDTO").apply {
            latitude = coordinates?.get(1) ?: 0.0
            longitude = coordinates?.get(0) ?: 0.0
        }
    }

}