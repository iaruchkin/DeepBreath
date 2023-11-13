package com.iaruchkin.deepbreath.data.model.aqi_cn

data class Daily(
    val o3: List<O3>,
    val pm10: List<Pm10>,
    val pm25: List<Pm10>,
    val uvi: List<Uvi>
)