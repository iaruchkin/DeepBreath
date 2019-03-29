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
import com.iaruchkin.deepbreath.room.ConditionEntity;
import com.iaruchkin.deepbreath.room.ConverterAqi;
import com.iaruchkin.deepbreath.room.ConverterCondition;
import com.iaruchkin.deepbreath.room.ConverterForecast;
import com.iaruchkin.deepbreath.room.ConverterWeather;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;
import com.iaruchkin.deepbreath.utils.LangUtils;

import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DetailPresenter extends BasePresenter<AqiView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private ForecastEntity forecastEntity;
    private WeatherEntity weatherEntity;
    private ConditionEntity conditionEntity;
    private AqiEntity aqiEntity;

    private WeatherApi weatherApi;
    private AqiApi aqiApi;

    private final String PRESENTER_WEATHER_TAG = "[detail - presenter]";

    private String ID_FORECAST;
    private String ID_WEATHER;
    private String ID_AQI;
    private String ID_CONDITION;
    private int VIEW_TYPE;

//    public void initData(String id){ //todo use in AQIFragment
//        if (forecastEntity == null || !forecastEntity.getId().equals(id)) {
//            loadData();
//        } else {
//            getViewState().showForecastData(forecastEntity);
//            getViewState().showAqiData(aqiEntity);
//            getViewState().showWeatherData(weatherEntity);
//        }
//    }

    public DetailPresenter(String idForecast, String idWeather, String idAqi, String idCondition, int viewType) {
        ID_FORECAST = idForecast;
        ID_WEATHER = idWeather;
        ID_AQI = idAqi;
        ID_CONDITION = idCondition;
        VIEW_TYPE = viewType;
    }

    @Override
    protected void onFirstViewAttach() {
        setView();
    }

    private void setView(){
        if (VIEW_TYPE == 0){
            loadCurrent();
            getViewState().showState(State.Current);
        } else {
            loadForecast();
            getViewState().showState(State.Forecast);
        }
    }

    private void loadCurrent(){
        loadWeatherFromDb(ID_WEATHER);
        loadAqiFromDb(ID_AQI);
        loadConditionFromDb(ID_CONDITION);
    }

    private void loadForecast(){
        loadForecastFromDb(ID_FORECAST);
        loadConditionFromDb(ID_CONDITION);
    }


    private void loadWeatherFromDb(String id){
//        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterWeather
                .getDataById(context, id))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWeatherData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    private void loadAqiFromDb(String id){
//        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .getDataById(context, id))              //todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateAqiData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load AqiData from db");
    }

    private void loadForecastFromDb(String id){
//        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterForecast
                .getDataById(context, id))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateForecastData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load Forecast from db");
    }


    private void updateWeatherData(@Nullable WeatherEntity data) {
//        getViewState().showWeatherData(data);
        weatherEntity = data;
        updateData();
//        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.getId() + " / " + data.getLocation());
        Log.i(PRESENTER_WEATHER_TAG, "updateWeatherData executed on thread: " + Thread.currentThread().getName());
    }

    private void updateForecastData(@Nullable ForecastEntity data) {
//        getViewState().showForecastData(data);
        forecastEntity = data;
        updateData();
//        getViewState().showState(State.Forecast);
//        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded ForecastData from DB: " + data.getId() + " / " + data.getLocationName());
        Log.i(PRESENTER_WEATHER_TAG, "updateForecastData executed on thread: " + Thread.currentThread().getName());

    }

    private void updateAqiData(@Nullable AqiEntity data) {
//        getViewState().showAqiData(data);
        aqiEntity = data;
        updateData();
//        getViewState().showState(State.HasData);
        Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data.getId() + " / " + data.getAqi());
        Log.i(PRESENTER_WEATHER_TAG, "updateAqiData executed on thread: " + Thread.currentThread().getName());
    }


    //condition

    private void loadConditionFromDb(String id){
//        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterCondition
                .getDataById(context, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateConditionData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    private void updateConditionData(@Nullable ConditionEntity data) {
            Log.w(PRESENTER_WEATHER_TAG, "init condition");
        conditionEntity = data;
        updateData();

//            getViewState().showConditionData(data);
//            getViewState().showState(State.HasData);
            Log.i(PRESENTER_WEATHER_TAG, "loaded condition from DB: " + data.getId() + " / " + data.getDayText());
            Log.i(PRESENTER_WEATHER_TAG, "update condition executed on thread: " + Thread.currentThread().getName());
    }

    private void handleError(Throwable th) {
//        getViewState().showState(State.NetworkError);
        Log.e(PRESENTER_WEATHER_TAG, th.getMessage(), th);
        Log.e(PRESENTER_WEATHER_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }

    private void updateData(){
        if (weatherEntity != null && aqiEntity != null && conditionEntity != null){
                    getViewState().showData(weatherEntity, aqiEntity, conditionEntity);
        }else if (forecastEntity != null && conditionEntity != null) {
            getViewState().showForecastData(forecastEntity, conditionEntity);
        }

    }

}
