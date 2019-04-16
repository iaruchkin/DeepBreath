package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.presentation.view.DetailView;
import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.converters.ConverterAqi;
import com.iaruchkin.deepbreath.room.converters.ConverterCondition;
import com.iaruchkin.deepbreath.room.converters.ConverterForecast;
import com.iaruchkin.deepbreath.room.converters.ConverterWeather;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class DetailPresenter extends BasePresenter<DetailView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private ForecastEntity forecastEntity;
    private WeatherEntity weatherEntity;
    private ConditionEntity conditionEntity;
    private AqiEntity aqiEntity;

    private final String PRESENTER_WEATHER_TAG = "[detail - presenter]";

    private String ID_FORECAST;
    private String ID_WEATHER;
    private String ID_AQI;
    private String ID_CONDITION;
    private int VIEW_TYPE;

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
        } else {
            loadForecast();
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

    /**work with database
     *
     * @param id
     */
    private void loadWeatherFromDb(String id){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterWeather
                .getDataById(context, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateWeatherData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.i(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    private void loadAqiFromDb(String id){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .getDataById(context, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateAqiData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.i(PRESENTER_WEATHER_TAG,"Load AqiData from db");
    }

    private void loadForecastFromDb(String id){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterForecast
                .getDataById(context, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateForecastData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.i(PRESENTER_WEATHER_TAG,"Load Forecast from db");
    }

    private void loadConditionFromDb(String id){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterCondition
                .getDataById(context, id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateConditionData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.i(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    /**setting data to app
     *
     * @param data
     */
    private void updateWeatherData(@Nullable WeatherEntity data) {
        weatherEntity = data;
        updateData();
        Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.getId() + " / " + data.getLocation());
        Log.i(PRESENTER_WEATHER_TAG, "updateWeatherData executed on thread: " + Thread.currentThread().getName());
    }

    private void updateForecastData(@Nullable ForecastEntity data) {
        forecastEntity = data;
        updateData();
        Log.i(PRESENTER_WEATHER_TAG, "loaded ForecastData from DB: " + data.getId() + " / " + data.getLocationName());
        Log.i(PRESENTER_WEATHER_TAG, "updateForecastData executed on thread: " + Thread.currentThread().getName());
    }

    private void updateAqiData(@Nullable AqiEntity data) {
        aqiEntity = data;
        updateData();
        Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data.getId() + " / " + data.getAqi());
        Log.i(PRESENTER_WEATHER_TAG, "updateAqiData executed on thread: " + Thread.currentThread().getName());
    }

    private void updateConditionData(@Nullable ConditionEntity data) {
        Log.w(PRESENTER_WEATHER_TAG, "init condition");
        conditionEntity = data;
        updateData();
        Log.i(PRESENTER_WEATHER_TAG, "loaded condition from DB: " + data.getId() + " / " + data.getDayText());
        Log.i(PRESENTER_WEATHER_TAG, "update condition executed on thread: " + Thread.currentThread().getName());
    }

    private void updateData(){
        if (weatherEntity != null && aqiEntity != null && conditionEntity != null){
            getViewState().showTodayData(weatherEntity, aqiEntity, conditionEntity);
        }else if (forecastEntity != null && conditionEntity != null) {
            getViewState().showForecastData(forecastEntity, conditionEntity);
        }
    }

    /**handling error
     *
     * @param th
     */
    private void handleError(Throwable th) {
        Log.e(PRESENTER_WEATHER_TAG, th.getMessage(), th);
        Log.e(PRESENTER_WEATHER_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }

}
