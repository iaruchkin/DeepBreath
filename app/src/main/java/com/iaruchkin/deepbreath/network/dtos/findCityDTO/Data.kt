package com.iaruchkin.deepbreath.network.dtos.findCityDTO


import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("aqi")
        var aqi: String,
        @SerializedName("station")
        var station: Station,
        @SerializedName("time")
        var time: Time,
        @SerializedName("uid")
        var uid: Int
)