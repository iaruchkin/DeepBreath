package com.iaruchkin.deepbreath.network.dtos.aqicnDTO

import android.location.Location
import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("geo")
    private var geo: List<Double>? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("url")
    var url: String? = null

    fun getCoordinates(): Location {
        return Location("aqicnDTO").apply {
            latitude = geo?.get(0) ?: 0.0
            longitude = geo?.get(1) ?: 0.0
        }
    }
}