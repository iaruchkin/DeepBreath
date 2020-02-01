package com.iaruchkin.deepbreath.network.parsers;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.network.endpoints.FindCityEndpoint;
import com.iaruchkin.deepbreath.network.interceptors.AqiApiKeyInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindCityApi {

    private static FindCityApi aqiApi;
    private static final String URL = "https://api.waqi.info/";
    private FindCityEndpoint findCityEndpoint;

    public static synchronized FindCityApi getInstance() {
        if (aqiApi == null) {
            aqiApi = new FindCityApi();
        }
        return aqiApi;
    }

    private FindCityApi() {
        final OkHttpClient client = builtClient();
        final Retrofit retrofit = builtRertofit(client);

        findCityEndpoint = retrofit.create(FindCityEndpoint.class);
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
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(AqiApiKeyInterceptor.create())
                .build();
    }

    @NonNull
    public FindCityEndpoint findCityEndpoint() {
        return findCityEndpoint;
    }
}
