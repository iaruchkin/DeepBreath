package com.iaruchkin.deepbreath.di

import com.iaruchkin.deepbreath.data.datasource.remote.ApiURL.API_KEY
import okhttp3.Interceptor
import okhttp3.Response


class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("token", API_KEY)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}