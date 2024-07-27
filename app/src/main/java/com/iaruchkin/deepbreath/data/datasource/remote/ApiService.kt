package com.iaruchkin.deepbreath.data.datasource.remote

import com.iaruchkin.deepbreath.data.model.AqiCn
import retrofit2.http.GET

interface ApiService {

    @GET("feed/here/")
    suspend fun aqiDetail(
//        @Query("token") token: String = ApiURL.API_KEY
    ): AqiCn

}