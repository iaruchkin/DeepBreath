package com.iaruchkin.deepbreath.data.model.aqi_cn

data class Daily(
    val o3: List<Value>,
    val pm10: List<Value>,
    val pm25: List<Value>,
    val uvi: List<Uvi>
)