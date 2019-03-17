package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiApi;
import com.iaruchkin.deepbreath.network.AqiResponse;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.network.WeatherResponse;
import com.iaruchkin.deepbreath.presentation.view.ForecastView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConverterAqi;
import com.iaruchkin.deepbreath.room.ConverterForecast;
import com.iaruchkin.deepbreath.room.ConverterWeather;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private WeatherApi weatherApi;
    private AqiApi aqiApi;

    private final String PRESENTER_WEATHER_TAG = "[list - presenter]";

    private String weatherCurrentLocation = "auto:ip";
    private String aqiCurrentLocation = "here";

    @Override
    protected void onFirstViewAttach() {
            loadData(true, null);
    }

    public void loadData(Boolean forceload, Location location){
        if(location != null) {
        aqiCurrentLocation = String.format(Locale.ENGLISH, "geo:%s;%s", location.getLatitude(), location.getLongitude());
        weatherCurrentLocation = String.format(Locale.ENGLISH, "%s,%s", location.getLatitude(), location.getLongitude());
        }

        if(!forceload) {
            loadForecastFromDb(weatherCurrentLocation);
            loadWeatherFromDb(weatherCurrentLocation);
            loadAqiFromDb(aqiCurrentLocation);
        }else {
            loadForecastFromNet(weatherCurrentLocation);
            loadAqiFromNet(aqiCurrentLocation);
        }
//        loadDummy();
    }

    private void loadDummy(){
        List<ForecastEntity> weatherEntities = new ArrayList<>();
        ForecastEntity dummy = new ForecastEntity();
        dummy.setLocationName("Moscow");
        dummy.setId("1qwe");
        dummy.setDate("02/03/2019");
        dummy.setAvgtemp_c(30.1);

        weatherEntities.add(dummy);
        weatherEntities.add(dummy);

        getViewState().showForecastData(weatherEntities);
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

    private void loadForecastFromDb(String option){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterForecast
                .loadDataFromDb(context))//todo get data by location!!! bug with multiple location forecast
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateForecastData(data, option), this::handleError);
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
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for weatherCurrentLocation : " + option);
            loadForecastFromNet(option); //todo check this
        }else {
            getViewState().showWeatherData(data);
            getViewState().showState(State.HasData);
            Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocation());
            Log.i(PRESENTER_WEATHER_TAG, "updateForecastData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void updateForecastData(@Nullable List<ForecastEntity> data, String option) {
        if (data.size()==0){
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for weatherCurrentLocation : " + option);
            loadForecastFromNet(option);
        }else {
            getViewState().showForecastData(data);
            getViewState().showState(State.HasData);
            Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocationName());
            Log.i(PRESENTER_WEATHER_TAG, "updateForecastData executed on thread: " + Thread.currentThread().getName());
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

    private void loadForecastFromNet(@NonNull String option){
        Log.e(PRESENTER_WEATHER_TAG,"Load Weather from net presenter");

        getViewState().showState(State.Loading);
        final Disposable disposable = WeatherApi.getInstance()
                .weatherEndpoint()
                .get(option)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    updateForecastDB(response, response.getLocation().getName());
                    updateWeatherDB(response, response.getLocation().getName());
                    },
                        this::handleError);

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

    private void updateWeatherDB(WeatherResponse response, String option) {
        if (response.getCurrent() == null) { //todo check this
                getViewState().showState(State.HasNoData);
        } else {
            Disposable saveWeatherToDb = Single.fromCallable(() -> response)
                    .subscribeOn(Schedulers.io())
                    .map(weatherDTO -> {
                        String location = weatherDTO.getLocation().getName();
                        ConverterWeather.saveAllDataToDb(context,
                                ConverterWeather.dtoToDao(weatherDTO, option),location);
                        return ConverterWeather.loadDataFromDb(context);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            weatherEntities -> {
                                getViewState().showWeatherData(weatherEntities);
                                Log.i(PRESENTER_WEATHER_TAG, "loaded weather from NET to DB: " + weatherEntities.get(0).getId() + " / " +  weatherEntities.get(0).getLocation());
                            });
            disposeOnDestroy(saveWeatherToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void updateForecastDB(WeatherResponse response, String option) {
        if (response.getForecast().getForecastday().size()==0) {
            getViewState().showState(State.HasNoData);
        } else {
            Disposable saveForecastToDb = Single.fromCallable(() -> response)
                    .subscribeOn(Schedulers.io())
                    .map(weatherDTO -> {
                        String location = weatherDTO.getLocation().getName();
                        ConverterForecast.saveAllDataToDb(context,
                                ConverterForecast.dtoToDao(weatherDTO, option),location);
                        return ConverterForecast.loadDataFromDb(context);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            forecastEntities -> {
                                getViewState().showForecastData(forecastEntities);
                                Log.i(PRESENTER_WEATHER_TAG, "loaded weather from NET to DB: " + forecastEntities.get(0).getId() + " / " +  forecastEntities.get(0).getLocationName());
                            });
            disposeOnDestroy(saveForecastToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void updateAqiDB(AqiResponse response, String location) {
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
