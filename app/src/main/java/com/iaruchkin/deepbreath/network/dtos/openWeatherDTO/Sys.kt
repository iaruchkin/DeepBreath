package com.iaruchkin.deepbreath.network.dtos.openWeatherDTO


import com.google.gson.annotations.SerializedName

data class Sys(
        @SerializedName("pod")
        val pod: String
)