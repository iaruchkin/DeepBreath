package com.iaruchkin.deepbreath.network.endpoints;

import com.iaruchkin.deepbreath.network.dtos.AqiAvResponse;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AqiAvEndpoint {

    @GET("/v2/nearest_city")
    Single<AqiAvResponse> get();

    @GET("/v2/nearest_city/")
    Single<AqiAvResponse> get(@Query("lat") @NonNull double lat,
                              @Query("lon") @NonNull double lon);

}


