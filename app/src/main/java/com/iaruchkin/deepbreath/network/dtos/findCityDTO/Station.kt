package com.iaruchkin.deepbreath.network.dtos.findCityDTO


import com.google.gson.annotations.SerializedName

data class Station(
        @SerializedName("geo")
        var geo: List<Double>?,
        @SerializedName("name")
        var name: String,
        @SerializedName("url")
        var url: String
)