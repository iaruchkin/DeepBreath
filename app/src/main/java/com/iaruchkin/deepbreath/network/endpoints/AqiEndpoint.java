package com.iaruchkin.deepbreath.network.endpoints;

import com.iaruchkin.deepbreath.network.dtos.AqiResponse;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AqiEndpoint {

    @GET("/feed/{location}/")
    Single<AqiResponse> get(@Path("location") @NonNull String section);
}


