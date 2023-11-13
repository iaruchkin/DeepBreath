package com.iaruchkin.deepbreath.data.model.aqi_cn

data class City(
    val geo: List<Double>,
    val location: String,
    val name: String,
    val url: String
)