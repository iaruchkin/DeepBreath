package com.iaruchkin.deepbreath.network;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherEndpoint {

    @GET("v1/forecast.json")
    Single<WeatherResponse> get(@Query("q") @NonNull String location);
}


