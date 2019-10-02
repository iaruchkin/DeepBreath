package com.iaruchkin.deepbreath.network.dtos.openWeatherDTO


import com.google.gson.annotations.SerializedName

data class Clouds(
        @SerializedName("all")
        val all: Int
)