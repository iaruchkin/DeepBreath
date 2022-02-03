package com.iaruchkin.deepbreath.core

data class BuildConfigProvider(
    val debug: Boolean,
    val appId: String,
    val buildType: String,
    val flavor: String,
    val versionCode: Int,
    val versionName: String,
)