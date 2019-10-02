package com.iaruchkin.deepbreath.network.endpoints;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.network.dtos.OpenWeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherEndpoint {
    @GET("2.5/forecast")
//    Single<OpenWeatherResponse> get(@Query("lat") @NonNull String location);//todo fix
    Single<OpenWeatherResponse> get(@Query("lat") @NonNull String lat, @Query("lon") @NonNull String lon);//todo fix
}


