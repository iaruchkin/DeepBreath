package com.iaruchkin.deepbreath.network.parsers;

import com.iaruchkin.deepbreath.network.endpoints.AqiAvEndpoint;
import com.iaruchkin.deepbreath.network.interceptors.AqiAvApiKeyInterceptor;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AqiAvApi {

    private static AqiAvApi aqiAvApi;
    private static final String URL = "https://api.airvisual.com/";
    private AqiAvEndpoint aqiAvEndpoint;

    public static synchronized AqiAvApi getInstance(){
        if (aqiAvApi == null){
            aqiAvApi = new AqiAvApi();
        }
        return aqiAvApi;
    }

    private AqiAvApi(){
        final OkHttpClient client = builtClient();
        final Retrofit retrofit = builtRertofit(client);

        aqiAvEndpoint = retrofit.create(AqiAvEndpoint.class);
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
                .addInterceptor(AqiAvApiKeyInterceptor.create())
                .build();
    }

    @NonNull
    public AqiAvEndpoint aqiAvEndpoint() {
        return aqiAvEndpoint;
    }
}
