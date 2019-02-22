package com.iaruchkin.deepbreath.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class ApiKeyInterceptor implements Interceptor {

//https://api.waqi.info/feed/here/?token=8ec682b91914d1b760576870360c7217b6224104
    private static final String API_KEY = "8ec682b91914d1b760576870360c7217b6224104";
    private static final String API_KEY_HEADER_NAME = "token";


    public static ApiKeyInterceptor create() {
        return new ApiKeyInterceptor();
    }


    private ApiKeyInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request requestWithoutApiKey = chain.request();

        final HttpUrl url = requestWithoutApiKey.url()
                .newBuilder()
                .addQueryParameter(API_KEY_HEADER_NAME, API_KEY)
                .build();

        final Request requestWithAttachedApiKey = requestWithoutApiKey.newBuilder()
                .url(url)
                .build();

        return chain.proceed(requestWithAttachedApiKey);
    }
}
