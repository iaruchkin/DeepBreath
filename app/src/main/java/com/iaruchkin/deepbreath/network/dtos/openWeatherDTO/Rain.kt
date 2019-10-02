package com.iaruchkin.deepbreath.network.dtos.openWeatherDTO


import com.google.gson.annotations.SerializedName

data class Rain(
        @SerializedName("3h")
        val h: Double
)