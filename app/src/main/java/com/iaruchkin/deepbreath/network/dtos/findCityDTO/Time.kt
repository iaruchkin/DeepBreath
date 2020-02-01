package com.iaruchkin.deepbreath.network.dtos.findCityDTO


import com.google.gson.annotations.SerializedName

data class Time(
        @SerializedName("stime")
        var stime: String,
        @SerializedName("tz")
        var tz: String,
        @SerializedName("vtime")
        var vtime: Int
)