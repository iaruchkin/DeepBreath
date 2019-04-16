package com.iaruchkin.deepbreath.network.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.iaruchkin.deepbreath.common.ApiKeys.AQIAV_API_KEY;
import static com.iaruchkin.deepbreath.common.ApiKeys.WEATHER_API_KEY;

public final class AqiAvApiKeyInterceptor implements Interceptor {

    private static final String API_KEY_HEADER_NAME = "key";

    public static AqiAvApiKeyInterceptor create() {
        return new AqiAvApiKeyInterceptor();
    }

    private AqiAvApiKeyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request requestWithoutApiKey = chain.request();

        final HttpUrl url = requestWithoutApiKey.url()
                .newBuilder()
                .addQueryParameter(API_KEY_HEADER_NAME, AQIAV_API_KEY)
                .build();

        final Request requestWithAttachedApiKey = requestWithoutApiKey.newBuilder()
                .url(url)
                .build();
        Log.w("URL AQIAV", requestWithAttachedApiKey.toString());

        return chain.proceed(requestWithAttachedApiKey);
    }
}
