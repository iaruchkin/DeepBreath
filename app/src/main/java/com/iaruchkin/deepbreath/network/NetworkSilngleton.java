package com.iaruchkin.deepbreath.network;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkSilngleton {

    private static NetworkSilngleton networkSilngleton;
    private static final String URL = "https://api.waqi.info/";
    private AirQualityEndpoint airQualityEndpoint;

    public static synchronized NetworkSilngleton getInstance(){
        if (networkSilngleton == null){
            networkSilngleton = new NetworkSilngleton();
        }
        return networkSilngleton;
    }

    private NetworkSilngleton(){
        final OkHttpClient client = builtClient();
        final Retrofit retrofit = builtRertofit(client);

        airQualityEndpoint = retrofit.create(AirQualityEndpoint.class);
    }

    private Retrofit builtRertofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient builtClient(){

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(ApiKeyInterceptor.create())
                .build();
    }

    @NonNull
    public AirQualityEndpoint airQuality() {
        return airQualityEndpoint;
    }
}
