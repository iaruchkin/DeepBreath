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
import com.iaruchkin.deepbreath.room.ForecastEntity;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class AqiPresenter extends BasePresenter<AqiView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private WeatherApi weatherApi;
    private ForecastEntity forecastEntity;

    private AqiApi aqiApi;
    private AqiEntity aqiEntity;

    private final String DEFAULT_LOCATION = "here";
    private final String FORECAST = "forecast";
    private final String PRESENTER_WEATHER_TAG = "[detail presenter]";

    public void initData(String id){ //todo use in AQIFragment
        if (forecastEntity == null || !forecastEntity.getId().equals(id)) {
            loadData();
        } else {
            getViewState().setWeatherData(forecastEntity);
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
        Disposable loadFromDb = Single.fromCallable(() -> ConverterForecast
                .getDataById(context, "2019-03-14Moscow"))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateWeatherData(data, option), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");

    }

    private void loadAqiFromDb(String location){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .getDataById(context, "114572019-03-14 06:00:00"))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateAqiData(data, location), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load AqiData from db");
    }

    private void updateWeatherData(ForecastEntity data, String option) {
        getViewState().setWeatherData(data); //todo get single item not list
        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.getId() + " / " + data.getLocationName());
        Log.i(PRESENTER_WEATHER_TAG, "updateWeatherData executed on thread: " + Thread.currentThread().getName());

    }

    private void updateAqiData(AqiEntity data, String location) {
        getViewState().setAqiData(data);
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
