package com.iaruchkin.deepbreath.network.parsers;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.data.aqi.remote.AqiEndpoint;
import com.iaruchkin.deepbreath.network.interceptors.AqiApiKeyInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AqiApi {

    private static AqiApi aqiApi;
    private static final String URL = "https://api.waqi.info/";
    private AqiEndpoint aqiEndpoint;

    public static synchronized AqiApi getInstance(){
        if (aqiApi == null){
            aqiApi = new AqiApi();
        }
        return aqiApi;
    }

    private AqiApi(){
        final OkHttpClient client = builtClient();
        final Retrofit retrofit = builtRertofit(client);

        aqiEndpoint = retrofit.create(AqiEndpoint.class);
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
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(AqiApiKeyInterceptor.create())
                .build();
    }

    @NonNull
    public AqiEndpoint aqiEndpoint() {
        return aqiEndpoint;
    }
}
