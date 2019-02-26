package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.network.WeatherResponse;
import com.iaruchkin.deepbreath.presentation.view.WeatherListView;
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
    private WeatherApi restApi;
    private final String DEFAULT_LOCATION = "here";
    private final String FORECAST = "forecast";

    public WeatherListPresenter(@NonNull WeatherApi instance) {
        this.restApi = instance;
    }

    @Override
    protected void onFirstViewAttach() {
        loadFromDb(FORECAST);
    }

    public void loadData(String location){
        loadFromDb(location);
//        loadDummy();

    }

    public void forceLoadData(String location){
        loadFromNet(location);
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

        getViewState().showData(weatherEntities);
    }

    private void loadFromDb(String location){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterWeather
                .loadDataFromDb(context, location))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateData(data, location), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(WEATHER_LIST_TAG,"Load from db presenter");

    }

    private void updateData(@Nullable List<WeatherEntity> data, String location) {
        if (data.size()==0){
            loadFromNet(location);
            Log.i(WEATHER_LIST_TAG, "there is no data for location : " + location);
        }else {
            getViewState().showData(data);
            getViewState().showState(State.HasData);
            Log.i(WEATHER_LIST_TAG, "loaded from DB: " + data.get(0).getLocation() + " / " + data.get(0).getId());
            Log.i(WEATHER_LIST_TAG, "updateData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void loadFromNet(@NonNull String location){
        Log.e(WEATHER_LIST_TAG,"Load from net start presenter");

        getViewState().showState(State.Loading);
        final Disposable disposable = WeatherApi.getInstance()
                .weatherEndpoint()
                .get(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateDB(response, location), this::handleError);
        disposeOnDestroy(disposable);
    }

    private void updateDB(WeatherResponse response , String location) {
        if (response.getForecast().getForecastday().size()==0) {
                getViewState().showState(State.HasNoData);
        } else {
            Disposable saveDataToDb = Single.fromCallable(() -> response.getForecast().getForecastday())
                    .subscribeOn(Schedulers.io())
                    .map(aqiDTO -> {
                        ConverterWeather.saveAllDataToDb(context,
                                ConverterWeather.dtoToDao(aqiDTO, location),location);
                        return ConverterWeather.loadDataFromDb(context, location);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            weatherEntities -> {
                                getViewState().showData(weatherEntities);
                                Log.i(WEATHER_LIST_TAG, "loaded from NET to DB: " + weatherEntities.get(0).getLocation() + " / " + weatherEntities.get(0).getId());
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
