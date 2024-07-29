package com.iaruchkin.deepbreath.data.model

import com.iaruchkin.deepbreath.data.model.aqi_cn.AqiData

data class AqiCn(
    val data: AqiData,
    val status: String
)