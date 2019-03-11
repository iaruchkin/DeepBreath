package com.iaruchkin.deepbreath.network;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IpAqiEndpoint {

    @GET("/feed/{location}/")
    Single<AqiResponse> get(@Path("location") @NonNull String section);
}


