package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiApi;
import com.iaruchkin.deepbreath.network.AqiResponse;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.network.WeatherResponse;
import com.iaruchkin.deepbreath.presentation.view.AqiView;
import com.iaruchkin.deepbreath.presentation.view.WeatherListView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConverterAqi;
import com.iaruchkin.deepbreath.room.ConverterWeather;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.iaruchkin.deepbreath.ui.MainActivity.WEATHER_LIST_TAG;

@InjectViewState
public class AqiPresenter extends BasePresenter<AqiView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private WeatherApi weatherApi;
    private WeatherEntity weatherEntity;

    private AqiApi aqiApi;
    private AqiEntity aqiEntity;

    private final String DEFAULT_LOCATION = "here";
    private final String FORECAST = "forecast";

    public void initData(String id){ //todo use in AQIFragment
        if (weatherEntity == null || !weatherEntity.getId().equals(id)) {
            loadData();
        } else {
            getViewState().setWeatherData(weatherEntity);
            getViewState().setAqiData(aqiEntity);
        }
    }

    @Override
    protected void onFirstViewAttach() {
        loadData();
    }

    public void loadData(){
        loadWeatherFromDb(FORECAST);
        loadAqiFromDb(DEFAULT_LOCATION);
    }

    private void loadWeatherFromDb(String option){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterWeather
                .loadDataFromDb(context, option))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateWeatherData(data, option), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(WEATHER_LIST_TAG,"Load WeatherData from db presenter");

    }

    private void loadAqiFromDb(String location){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .loadDataFromDb(context, location))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateAqiData(data, location), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(WEATHER_LIST_TAG,"Load AqiData from db presenter");

    }

    private void updateWeatherData(@Nullable List<WeatherEntity> data, String option) {
        getViewState().setWeatherData(data.get(0)); //todo get single item not list
        getViewState().showState(State.HasData);
        Log.i(WEATHER_LIST_TAG, "loaded WeatherData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocation());
Log.i(WEATHER_LIST_TAG, "updateWeatherData executed on thread: " + Thread.currentThread().getName());

    }

    private void updateAqiData(@Nullable AqiEntity data, String location) {
        getViewState().setAqiData(data);
        getViewState().showState(State.HasData);
        Log.i(WEATHER_LIST_TAG, "loaded AqiData from DB: " + data.getId() + " / " + data.getAqi());
        Log.i(WEATHER_LIST_TAG, "updateAqiData executed on thread: " + Thread.currentThread().getName());
    }

    private void handleError(Throwable th) {
        getViewState().showState(State.NetworkError);
        Log.e(WEATHER_LIST_TAG, th.getMessage(), th);
        Log.e(WEATHER_LIST_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }
}
