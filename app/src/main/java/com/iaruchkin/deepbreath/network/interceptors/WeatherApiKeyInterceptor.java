package com.iaruchkin.deepbreath.network.interceptors;

import android.util.Log;

import com.iaruchkin.deepbreath.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class WeatherApiKeyInterceptor implements Interceptor {

    private static final String API_KEY_HEADER_NAME = "key";
    private static final String TIME_HEADER_NAME = "days";
    private static final String TIME = "10";

    public static WeatherApiKeyInterceptor create() {
        return new WeatherApiKeyInterceptor();
    }


    private WeatherApiKeyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request requestWithoutApiKey = chain.request();

        final HttpUrl url = requestWithoutApiKey.url()
                .newBuilder()
                .addQueryParameter(API_KEY_HEADER_NAME, BuildConfig.WEATHER_API_KEY)
                .addQueryParameter(TIME_HEADER_NAME, TIME)
                .build();

        final Request requestWithAttachedApiKey = requestWithoutApiKey.newBuilder()
                .url(url)
                .build();
        Log.w("URL WEATHER", requestWithAttachedApiKey.toString());

        return chain.proceed(requestWithAttachedApiKey);
    }
}
