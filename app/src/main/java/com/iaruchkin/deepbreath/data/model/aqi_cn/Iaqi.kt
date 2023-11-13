package com.iaruchkin.deepbreath.data.model.aqi_cn

data class Iaqi(
    val co: Co,
    val h: H,
    val no2: No2,
    val p: P,
    val pm10: Pm10X,
    val pm25: Pm25X,
    val so2: So2,
    val t: T,
    val w: W
)