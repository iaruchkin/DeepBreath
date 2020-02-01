package com.iaruchkin.deepbreath.network.dtos


import com.google.gson.annotations.SerializedName
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data

data class CityList(
        @SerializedName("data")
        var cityList: List<Data>,
        @SerializedName("status")
        var status: String
)