package com.iaruchkin.deepbreath.network.endpoints;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.network.dtos.CityList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FindCityEndpoint {
    //https://api.waqi.info/search/?token=apikey&keyword=москва
    @GET("/search/")
    Single<CityList> get(@Query("keyword") @NonNull String city);
}


