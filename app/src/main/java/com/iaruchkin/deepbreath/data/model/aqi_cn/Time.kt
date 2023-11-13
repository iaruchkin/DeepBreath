package com.iaruchkin.deepbreath.data.model.aqi_cn

data class Time(
    val iso: String,
    val s: String,
    val tz: String,
    val v: Int
)