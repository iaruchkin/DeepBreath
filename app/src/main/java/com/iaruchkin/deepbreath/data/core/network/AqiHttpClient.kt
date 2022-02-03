package com.iaruchkin.deepbreath.data.core

import com.iaruchkin.deepbreath.core.AqiUrlProvider
import com.iaruchkin.deepbreath.data.aqi.remote.AqiEndpoint
import com.iaruchkin.deepbreath.data.core.network.QueryInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface AqiHttpClient {

    val aqiApi: AqiEndpoint
}

class AqiHttpClientImpl @Inject constructor(
    aqiUrlProvider: AqiUrlProvider,
) : AqiHttpClient {

    private val client = OkHttpClient.Builder()
        .addInterceptor(QueryInterceptor(hashMapOf("token" to aqiUrlProvider.apiKey)))
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(aqiUrlProvider.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val aqiApi: AqiEndpoint by lazy(LazyThreadSafetyMode.NONE) { retrofit.create(
        AqiEndpoint::class.java) }
}