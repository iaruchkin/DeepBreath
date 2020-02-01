package com.iaruchkin.deepbreath.network.interceptors;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.iaruchkin.deepbreath.common.ApiKeys.OPEN_WEATHER_API_KEY;

public final class OpenWeatherApiKeyInterceptor implements Interceptor {

    private static final String API_KEY_HEADER_NAME = "appid";

    public static OpenWeatherApiKeyInterceptor create() {
        return new OpenWeatherApiKeyInterceptor();
    }


    private OpenWeatherApiKeyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request requestWithoutApiKey = chain.request();

        final HttpUrl url = requestWithoutApiKey.url()
                .newBuilder()
                .addQueryParameter(API_KEY_HEADER_NAME, OPEN_WEATHER_API_KEY)
                .build();

        final Request requestWithAttachedApiKey = requestWithoutApiKey.newBuilder()
                .url(url)
                .build();
        Log.w("URL WEATHER", requestWithAttachedApiKey.toString());

        return chain.proceed(requestWithAttachedApiKey);
    }
}
