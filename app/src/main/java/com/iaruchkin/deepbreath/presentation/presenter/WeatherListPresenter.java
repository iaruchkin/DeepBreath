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

    private final String PRESENTER_WEATHER_TAG = "[list - presenter]";
    private final String DEFAULT_LOCATION = "here";
    private final String FORECAST = "forecast";

    public WeatherListPresenter(@NonNull WeatherApi weatherInstance, AqiApi aqiInstance) {
        this.weatherApi = weatherInstance;
        this.aqiApi = aqiInstance;
    }

    @Override
    protected void onFirstViewAttach() {
        forceLoadData();
    }

    public void loadData(){
        loadWeatherFromDb(FORECAST);
        loadAqiFromDb(DEFAULT_LOCATION);
//        loadDummy();

    }

    public void forceLoadData(){
        loadWeatherFromNet(FORECAST);
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
                .loadDataFromDb(context))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateWeatherData(data, option), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");

    }

    private void loadAqiFromDb(String location){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .loadDataFromDb(context))              //todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateAqiData(data, location), this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load AqiData from db");

    }

    private void updateWeatherData(@Nullable List<WeatherEntity> data, String option) {
        if (data.size()==0){
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for option : " + option);
            loadWeatherFromNet(option);
        }else {
            getViewState().showWeatherData(data);
            getViewState().showState(State.HasData);
            Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocation());
            Log.i(PRESENTER_WEATHER_TAG, "updateWeatherData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void updateAqiData(@Nullable List<AqiEntity> data, String location) {
        if (data.size() == 0){
            Log.w(PRESENTER_WEATHER_TAG, "there is no AqiData for location : " + location);
            loadAqiFromNet(location);
        }else {
            getViewState().showAqiData(data);
            getViewState().showState(State.HasData);
            Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data.get(0).getId() + " / " + data.get(0).getAqi());
            Log.i(PRESENTER_WEATHER_TAG, "updateAqiData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void loadWeatherFromNet(@NonNull String option){
        Log.e(PRESENTER_WEATHER_TAG,"Load Weather from net presenter");

        getViewState().showState(State.Loading);
        final Disposable disposable = WeatherApi.getInstance()
                .weatherEndpoint()
                .get(option)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateWeatherDB(response, response.getLocation().name), this::handleError);
        disposeOnDestroy(disposable);
    }

    private void loadAqiFromNet(@NonNull String location){
        Log.e(PRESENTER_WEATHER_TAG,"Load AQI from net presenter");

        getViewState().showState(State.Loading);
        final Disposable disposable = AqiApi.getInstance()
                .airEndpoint()
                .get(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateAqiDB(response, response.getData().getCity().getName()), this::handleError);
        disposeOnDestroy(disposable);
    }

    private void updateWeatherDB(WeatherResponse response , String option) {
        if (response.getForecast().getForecastday().size()==0) {
                getViewState().showState(State.HasNoData);
        } else {
            Disposable saveDataToDb = Single.fromCallable(() -> response)
                    .subscribeOn(Schedulers.io())
                    .map(weatherDTO -> {
                        ConverterWeather.saveAllDataToDb(context,
                                ConverterWeather.dtoToDao(weatherDTO, option),weatherDTO.getLocation().name);
                        return ConverterWeather.loadDataFromDb(context);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            weatherEntities -> {
                                getViewState().showWeatherData(weatherEntities);
                                Log.i(PRESENTER_WEATHER_TAG, "loaded weather from NET to DB: " + weatherEntities.get(0).getId() + " / " +  weatherEntities.get(0).getLocation());
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
                        String city = aqiDTO.getCity().getName();
                        ConverterAqi.saveAllDataToDb(context,
                                ConverterAqi.dtoToDao(aqiDTO, city),city);
                        return ConverterAqi.loadDataFromDb(context);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            aqiEntities -> {
                                getViewState().showAqiData(aqiEntities);
                                Log.i(PRESENTER_WEATHER_TAG, "loaded aqi from NET to DB: " + aqiEntities.get(0).getId() + " / " + aqiEntities.get(0).getAqi());
                            });
            disposeOnDestroy(saveDataToDb);
            getViewState().showState(State.HasData);
        }
    }



    private void handleError(Throwable th) {
        getViewState().showState(State.NetworkError);
        Log.e(PRESENTER_WEATHER_TAG, th.getMessage(), th);
        Log.e(PRESENTER_WEATHER_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }
}
