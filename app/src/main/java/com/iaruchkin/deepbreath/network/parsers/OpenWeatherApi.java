package com.iaruchkin.deepbreath.network.parsers;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.network.endpoints.OpenWeatherEndpoint;
import com.iaruchkin.deepbreath.network.interceptors.OpenWeatherApiKeyInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherApi {

    private static OpenWeatherApi networkSilngleton;
    private static final String URL = "https://api.openweathermap.org/data/";
    private OpenWeatherEndpoint openWeatherEndpoint;

    public static synchronized OpenWeatherApi getInstance() {
        if (networkSilngleton == null) {
            networkSilngleton = new OpenWeatherApi();
        }
        return networkSilngleton;
    }

    private OpenWeatherApi() {
        final OkHttpClient client = builtClient();
        final Retrofit retrofit = builtRertofit(client);

        openWeatherEndpoint = retrofit.create(OpenWeatherEndpoint.class);
    }

    private Retrofit builtRertofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient builtClient() {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(OpenWeatherApiKeyInterceptor.create())
                .build();
    }

    @NonNull
    public OpenWeatherEndpoint openWeatherEndpoint() {
        return openWeatherEndpoint;
    }
}