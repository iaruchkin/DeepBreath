package com.iaruchkin.deepbreath.data.model.aqi_cn

data class Pm10(
    val avg: Int,
    val day: String,
    val max: Int,
    val min: Int
)