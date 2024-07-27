package com.iaruchkin.deepbreath.di

import android.util.Base64
import com.iaruchkin.deepbreath.data.datasource.remote.ApiURL.API_KEY_ENC
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = String(Base64.decode(API_KEY_ENC, Base64.DEFAULT))
        val url = chain.request().url.newBuilder()
            .addQueryParameter("token", token)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}