package com.iaruchkin.deepbreath.data.aqi.remote

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Aqi api of api.waqi.info
 */
interface AqiEndpoint {
    @GET("/feed/{location}/")
    suspend fun get(
        @Path("location") section: String
    ): AqiResponse
}