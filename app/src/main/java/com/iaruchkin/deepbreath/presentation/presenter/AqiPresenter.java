package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiApi;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.presentation.view.AqiView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConverterAqi;
import com.iaruchkin.deepbreath.room.ConverterForecast;
import com.iaruchkin.deepbreath.room.ConverterWeather;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AqiPresenter extends BasePresenter<AqiView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private WeatherApi weatherApi;
    private ForecastEntity forecastEntity;
    private WeatherEntity weatherEntity;

    private AqiApi aqiApi;
    private AqiEntity aqiEntity;

    private final String PRESENTER_WEATHER_TAG = "[detail - presenter]";

    private final String DEFAULT_LOCATION = "here";
    private final String FORECAST = "forecast";
    //todo set real data
    private String ID_FORECAST;
    private String ID_WEATHER;
    private String ID_AQI;

//    public void initData(String id){ //todo use in AQIFragment
//        if (forecastEntity == null || !forecastEntity.getId().equals(id)) {
//            loadData();
//        } else {
//            getViewState().showForecastData(forecastEntity);
//            getViewState().showAqiData(aqiEntity);
//            getViewState().showWeatherData(weatherEntity);
//        }
//    }

    public AqiPresenter(String idForecast, String idWeather, String idAqi) {
        ID_FORECAST = idForecast;
        ID_WEATHER = idWeather;
        ID_AQI = idAqi;
    }

    @Override
    protected void onFirstViewAttach() {
        loadData();
    }

    public void loadData(){
        loadForecastFromDb(FORECAST);
        loadWeatherFromDb(FORECAST);
        loadAqiFromDb(DEFAULT_LOCATION);
    }

    private void loadWeatherFromDb(String option){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterWeather
                .getDataById(context, ID_WEATHER))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateWeatherData(data, option), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    private void loadAqiFromDb(String location){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .getDataById(context, ID_AQI))              //todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateAqiData(data, location), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load AqiData from db");
    }

    private void loadForecastFromDb(String option){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterForecast
                .getDataById(context, ID_FORECAST))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateForecastData(data, option), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }


    private void updateWeatherData(@Nullable WeatherEntity data, String option) {
        getViewState().showWeatherData(data);
        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.getId() + " / " + data.getLocation());
        Log.i(PRESENTER_WEATHER_TAG, "updateForecastData executed on thread: " + Thread.currentThread().getName());
    }

    private void updateForecastData(@Nullable ForecastEntity data, String option) {
        getViewState().showForecastData(data);
        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.getId() + " / " + data.getLocationName());
        Log.i(PRESENTER_WEATHER_TAG, "updateForecastData executed on thread: " + Thread.currentThread().getName());

    }

    private void updateAqiData(@Nullable AqiEntity data, String location) {
        getViewState().showAqiData(data);
        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data.getId() + " / " + data.getAqi());
        Log.i(PRESENTER_WEATHER_TAG, "updateAqiData executed on thread: " + Thread.currentThread().getName());
    }

    private void handleError(Throwable th) {
        getViewState().showState(State.NetworkError);
        Log.e(PRESENTER_WEATHER_TAG, th.getMessage(), th);
        Log.e(PRESENTER_WEATHER_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }
}
