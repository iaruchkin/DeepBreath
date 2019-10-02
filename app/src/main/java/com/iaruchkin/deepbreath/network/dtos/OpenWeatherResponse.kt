package com.iaruchkin.deepbreath.network.dtos


import com.google.gson.annotations.SerializedName
import com.iaruchkin.deepbreath.network.dtos.openWeatherDTO.City
import com.iaruchkin.deepbreath.network.dtos.openWeatherDTO.X

data class OpenWeatherResponse(
        @SerializedName("city")
        val city: City,
        @SerializedName("cnt")
        val cnt: Int,
        @SerializedName("cod")
        val cod: String,
        @SerializedName("list")
        val list: List<X>,
        @SerializedName("message")
        val message: Double
)