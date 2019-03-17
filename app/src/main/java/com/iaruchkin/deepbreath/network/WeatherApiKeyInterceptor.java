package com.iaruchkin.deepbreath.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class WeatherApiKeyInterceptor implements Interceptor {

//    http://api.apixu.com/v1/forecast.json?key=b0fc1e34365e482ab57160541192402&q=Moscow&days=10
    private static final String API_KEY = "b0fc1e34365e482ab57160541192402";
    private static final String API_KEY_HEADER_NAME = "key";
    private static final String CITY_HEADER_NAME = "q";
    private static final String CITY = "Moscow";
    private static final String TIME_HEADER_NAME = "days";
    private static final String TIME = "10";

    private static final String GPS = "48.8567,2.3508";
    private static final String IP = "109.252.99.90";

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
                .addQueryParameter(API_KEY_HEADER_NAME, API_KEY)
//                .addQueryParameter(CITY_HEADER_NAME, CITY)
                .addQueryParameter(TIME_HEADER_NAME, TIME)
                .build();

        final Request requestWithAttachedApiKey = requestWithoutApiKey.newBuilder()
                .url(url)
                .build();
        Log.w("URL WEATHER", requestWithAttachedApiKey.toString());

        return chain.proceed(requestWithAttachedApiKey);
    }
}
