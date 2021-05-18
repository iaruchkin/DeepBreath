package com.iaruchkin.deepbreath.network.dtos.findCityDTO


import android.location.Location
import com.google.gson.annotations.SerializedName

class Station {
        @SerializedName("geo") private var geo: List<Double>? = null
        @SerializedName("name") var name: String? = null
        @SerializedName("url") var url: String? = null

        fun getCoordinates(): Location {
                return Location("findCity Location").apply {
                        latitude = geo?.get(0) ?: 0.0
                        longitude = geo?.get(1) ?: 0.0
                }
        }
}