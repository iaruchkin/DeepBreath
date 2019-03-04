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
public class WeatherListPresenter extends BasePresenter<WeatherListView> {
    private Context context = App.INSTANCE.getApplicationContext();
    private WeatherApi weatherApi;
    private AqiApi aqiApi;

    private final String DEFAULT_LOCATION = "here";
    private final String FORECAST = "forecast";

    public WeatherListPresenter(@NonNull WeatherApi weatherInstance, AqiApi aqiInstance) {
        this.weatherApi = weatherInstance;
        this.aqiApi = aqiInstance;
    }

    @Override
    protected void onFirstViewAttach() {
        forceLoadData(FORECAST);
    }

    public void loadData(String location){
        loadWeatherFromDb(location);
        loadAqiFromDb(DEFAULT_LOCATION);
//        loadDummy();

    }

    public void forceLoadData(String location){
        loadWeatherFromNet(location);
        loadAqiFromNet(DEFAULT_LOCATION);
//        loadDummy();
    }

    private void loadDummy(){
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        WeatherEntity dummy = new WeatherEntity();
        dummy.setLocation("Moscow");
        dummy.setId("1qwe");
        dummy.setDate("02/03/2019");
        dummy.setTemperature(30.1);

        weatherEntities.add(dummy);
        weatherEntities.add(dummy);

        getViewState().showWeatherData(weatherEntities);
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
        if (data.size()==0){
            loadWeatherFromNet(option);
            Log.i(WEATHER_LIST_TAG, "there is no WeatherData for option : " + option);
        }else {
            getViewState().showWeatherData(data);
            getViewState().showState(State.HasData);
            Log.i(WEATHER_LIST_TAG, "loaded WeatherData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocation());
            Log.i(WEATHER_LIST_TAG, "updateWeatherData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void updateAqiData(@Nullable AqiEntity data, String location) {
        if (data == null){
            loadAqiFromNet(location);
            Log.i(WEATHER_LIST_TAG, "there is no AqiData for location : " + location);
        }else {
            getViewState().showAqiData(data);
            getViewState().showState(State.HasData);
            Log.i(WEATHER_LIST_TAG, "loaded AqiData from DB: " + data.getId() + " / " + data.getAqi());
            Log.i(WEATHER_LIST_TAG, "updateAqiData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void loadWeatherFromNet(@NonNull String option){
        Log.e(WEATHER_LIST_TAG,"Load Weather from net start presenter");

        getViewState().showState(State.Loading);
        final Disposable disposable = WeatherApi.getInstance()
                .weatherEndpoint()
                .get(option)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateWeatherDB(response, option), this::handleError);
        disposeOnDestroy(disposable);
    }

    private void loadAqiFromNet(@NonNull String location){
        Log.e(WEATHER_LIST_TAG,"Load AQI from net start presenter");

        getViewState().showState(State.Loading);
        final Disposable disposable = AqiApi.getInstance()
                .airEndpoint()
                .get(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateAqiDB(response, location), this::handleError);
        disposeOnDestroy(disposable);
    }

    private void updateWeatherDB(WeatherResponse response , String option) {
        if (response.getForecast().getForecastday().size()==0) {
                getViewState().showState(State.HasNoData);
        } else {
            Disposable saveDataToDb = Single.fromCallable(() -> response.getForecast().getForecastday())
                    .subscribeOn(Schedulers.io())
                    .map(aqiDTO -> {
                        ConverterWeather.saveAllDataToDb(context,
                                ConverterWeather.dtoToDao(aqiDTO, option),option);
                        return ConverterWeather.loadDataFromDb(context, option);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            weatherEntities -> {
                                getViewState().showWeatherData(weatherEntities);
                                Log.i(WEATHER_LIST_TAG, "loaded weather from NET to DB: " + weatherEntities.get(0).getId() + " / " +  weatherEntities.get(0).getLocation());
                            });
            disposeOnDestroy(saveDataToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void updateAqiDB(AqiResponse response , String location) {
        if (response.getData() == null) {
            getViewState().showState(State.HasNoData);
        } else {
            Disposable saveDataToDb = Single.fromCallable(response::getData)
                    .subscribeOn(Schedulers.io())
                    .map(aqiDTO -> {
                        ConverterAqi.saveAllDataToDb(context,
                                ConverterAqi.dtoToDao(aqiDTO, location),location);
                        return ConverterAqi.loadDataFromDb(context, location);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            aqiEntities -> {
                                getViewState().showAqiData(aqiEntities);
                                Log.i(WEATHER_LIST_TAG, "loaded aqi from NET to DB: " + aqiEntities.getId() + " / " + aqiEntities.getAqi());
                            });
            disposeOnDestroy(saveDataToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void handleError(Throwable th) {
        getViewState().showState(State.NetworkError);
        Log.e(WEATHER_LIST_TAG, th.getMessage(), th);
        Log.e(WEATHER_LIST_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }
}
