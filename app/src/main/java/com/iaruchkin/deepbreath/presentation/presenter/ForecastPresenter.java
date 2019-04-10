package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiApi;
import com.iaruchkin.deepbreath.network.AqiResponse;
import com.iaruchkin.deepbreath.network.ConditionParser;
import com.iaruchkin.deepbreath.network.WeatherApi;
import com.iaruchkin.deepbreath.network.WeatherResponse;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.OfflineCondition.WeatherCondition;
import com.iaruchkin.deepbreath.presentation.view.ForecastView;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConditionEntity;
import com.iaruchkin.deepbreath.room.ConverterAqi;
import com.iaruchkin.deepbreath.room.ConverterCondition;
import com.iaruchkin.deepbreath.room.ConverterForecast;
import com.iaruchkin.deepbreath.room.ConverterWeather;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;
import com.iaruchkin.deepbreath.utils.LangUtils;
import com.iaruchkin.deepbreath.utils.PreferencesHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {
    private Context context = App.INSTANCE.getApplicationContext();

    private List<ForecastEntity> forecastEntity;
    private List<WeatherEntity> weatherEntity;
    private List<ConditionEntity> conditionEntity;
    private List<AqiEntity> aqiEntity;

    private final String PRESENTER_WEATHER_TAG = "[list - presenter]";

    private String weatherCurrentLocation = "auto:ip";
    private String aqiCurrentLocation = "here";
    private Boolean isGps = false;

    public ForecastPresenter(Boolean idGps) {
        isGps = idGps;
    }

    @Override
    protected void onFirstViewAttach() {
        loadData(false, PreferencesHelper.getLocation(context));
    }

    public void loadData(Boolean forceload, Location location){

        if(isGps){
            aqiCurrentLocation = PreferencesHelper.getAqiParameter(context);
            weatherCurrentLocation = PreferencesHelper.getWeatherParameter(context);
        }

        if(!forceload) {
            loadAqiFromDb(aqiCurrentLocation);
            loadForecastFromDb(weatherCurrentLocation);
            loadWeatherFromDb(weatherCurrentLocation);
            loadConditionFromDb();
        }else {
            loadAqiFromNet(aqiCurrentLocation);
            loadForecastFromNet(weatherCurrentLocation);
            loadCondition();
        }
    }

    /**work with database
     *
     * @param geo
     */
    private void loadWeatherFromDb(String geo){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterWeather
                .getDataByParameter(context, geo))//todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateWeatherData(data, geo), this::handleDbError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    private void loadForecastFromDb(String geo){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterForecast
                .getDataByParameter(context, geo))//todo get data by location!!! bug with multiple location forecast
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateForecastData(data, geo), this::handleDbError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    private void loadAqiFromDb(String geo){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterAqi
                .getDataByParameter(context, geo))              //todo real data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> updateAqiData(data, geo), this::handleDbError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load AqiData from db");
    }

    private void loadConditionFromDb(){
        Disposable loadFromDb = Single.fromCallable(() -> ConverterCondition
                .getDataByLang(context, LangUtils.getLangCode()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateConditionData, this::handleError);
        disposeOnDestroy(loadFromDb);
        Log.e(PRESENTER_WEATHER_TAG,"Load WeatherData from db");
    }

    /**check db responce
     *
     * @param data
     * @param geo
     */
    private void updateWeatherData(@Nullable List<WeatherEntity> data, String geo) {
        if (data.size()==0){
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for weatherCurrentLocation : " + geo);
            loadForecastFromNet(geo); //todo check this
        }else {
            weatherEntity = data;
            updateData();

            Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocation());
            Log.i(PRESENTER_WEATHER_TAG, "update WeatherData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void updateForecastData(@Nullable List<ForecastEntity> data, String option) {
        if (data.size()==0){
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for weatherCurrentLocation : " + option);
            loadForecastFromNet(option);
        }else {
            forecastEntity = data;
            updateData();

            Log.i(PRESENTER_WEATHER_TAG, "loaded ForecastData from DB: " + data.get(0).getId() + " / " + data.get(0).getLocationName());
            Log.i(PRESENTER_WEATHER_TAG, "update ForecastData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void updateAqiData(@Nullable List<AqiEntity> data, String location) {
        if (data.size() == 0){
            Log.w(PRESENTER_WEATHER_TAG, "there is no AqiData for location : " + location);
            loadAqiFromNet(location);
        }else {
            aqiEntity = data;
            updateData();

            Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data.get(0).getId() + " / " + data.get(0).getAqi());
            Log.i(PRESENTER_WEATHER_TAG, "update AqiData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void updateConditionData(@Nullable List<ConditionEntity> data) {
        if (data.size()==0){
            Log.w(PRESENTER_WEATHER_TAG, "init condition");
            loadCondition();
        }else {
            conditionEntity = data;
            updateData();

            Log.i(PRESENTER_WEATHER_TAG, "loaded condition from DB: " + data.get(0).getId() + " / " + data.get(0).getDayText());
            Log.i(PRESENTER_WEATHER_TAG, "update condition executed on thread: " + Thread.currentThread().getName());
        }
    }

    /**work with internet
     *
     * @param parameter
     */
    private void loadForecastFromNet(@NonNull String parameter){
        Log.e(PRESENTER_WEATHER_TAG,"Load Forecast from net presenter");
        getViewState().showState(State.Loading);

        final Disposable disposable = WeatherApi.getInstance()
                .weatherEndpoint()
                .get(parameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    updateForecastDB(response, parameter);
                    updateWeatherDB(response, parameter);
                    },
                        this::handleError);

        disposeOnDestroy(disposable);
    }

    private void loadAqiFromNet(@NonNull String parameter){
        Log.e(PRESENTER_WEATHER_TAG,"Load AQI from net presenter");
        getViewState().showState(State.Loading);

        final Disposable disposable = AqiApi.getInstance()
                .airEndpoint()
                .get(parameter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateAqiDB(response, parameter), this::handleError);//response.getData().getCity().getName()
        disposeOnDestroy(disposable);
    }

    private void loadCondition(){
        final Disposable disposable = Single.fromCallable(ConditionParser.getInstance()::conditions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateCondition, this::handleError);

        disposeOnDestroy(disposable);
    }

    /**update database
     *
     * @param response
     * @param parameter
     */
    private void updateWeatherDB(WeatherResponse response, String parameter) {
        if (response.getCurrent() == null) {
                getViewState().showState(State.HasNoData);
        } else {
            Disposable saveWeatherToDb = Single.fromCallable(() -> response)
                    .subscribeOn(Schedulers.io())
                    .map(weatherDTO -> {
                        ConverterWeather.saveAllDataToDb(context,
                                ConverterWeather.dtoToDao(weatherDTO, parameter),parameter);
                        return ConverterWeather.getDataByParameter(context, parameter);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            weatherEntities -> {
                                weatherEntity = weatherEntities;
                                updateData();
                                Log.i(PRESENTER_WEATHER_TAG, "loaded weather from NET to DB, size: " + weatherEntities.size());
                            });
            disposeOnDestroy(saveWeatherToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void updateForecastDB(WeatherResponse response, String parameter) {
        if (response.getForecast().getForecastday().size()==0) {
            getViewState().showState(State.HasNoData);
        } else {
            Disposable saveForecastToDb = Single.fromCallable(() -> response)
                    .subscribeOn(Schedulers.io())
                    .map(weatherDTO -> {
                        ConverterForecast.saveAllDataToDb(context,
                                ConverterForecast.dtoToDao(weatherDTO, parameter),parameter);
                        return ConverterForecast.getDataByParameter(context, parameter);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            forecastEntities -> {
                                forecastEntity = forecastEntities;
                                updateData();
                                Log.i(PRESENTER_WEATHER_TAG, "loaded forecast from NET to DB, size: " + forecastEntities.size());
                            });
            disposeOnDestroy(saveForecastToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void updateAqiDB(AqiResponse response, String parameter) {
        if (response.getData() == null) {
            getViewState().showState(State.HasNoData);
        } else {
            Disposable saveDataToDb = Single.fromCallable(response::getData)
                    .subscribeOn(Schedulers.io())
                    .map(aqiDTO -> {
                        ConverterAqi.saveAllDataToDb(context,
                                ConverterAqi.dtoToDao(aqiDTO, parameter),parameter);
                        return ConverterAqi.getDataByParameter(context, parameter);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            aqiEntities -> {
                                aqiEntity = aqiEntities;
                                updateData();
                                Log.i(PRESENTER_WEATHER_TAG, "loaded aqi from NET to DB, size: " + aqiEntities.size());
                            });
            disposeOnDestroy(saveDataToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void updateCondition(List<WeatherCondition> response) {
         Disposable saveDataToDb = Single.fromCallable(() -> response)
                 .subscribeOn(Schedulers.io())
                 .map(conditions -> {
                     ConverterCondition.saveAllDataToDb(context,
                             ConverterCondition.dtoToDao(conditions, LangUtils.getLangCode()));
                     return ConverterCondition.loadDataFromDb(context);
                 })
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(
                         conditionEntities -> {
                             conditionEntity = conditionEntities;
                             updateData();

                             Log.i(PRESENTER_WEATHER_TAG, "loaded condition from NET to DB, size: " + conditionEntities.size());
                         });
         disposeOnDestroy(saveDataToDb);
         getViewState().showState(State.HasData);
    }

    /**setting data objects
     *
     */
    private void updateData() {
        if (forecastEntity != null && weatherEntity != null && aqiEntity != null && conditionEntity != null) {
            getViewState().showData(forecastEntity, weatherEntity, aqiEntity, conditionEntity);
            getViewState().showState(State.HasData);
        }
    }

    /**handling errors
     *
     * @param th
     */
    private void handleError(Throwable th) {
        getViewState().showState(State.NetworkError);
        Log.e(PRESENTER_WEATHER_TAG, th.getMessage(), th);
        Log.e(PRESENTER_WEATHER_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }

    private void handleDbError(Throwable th) {
        getViewState().showState(State.DbError);
        Log.e(PRESENTER_WEATHER_TAG, th.getMessage(), th);
        Log.e(PRESENTER_WEATHER_TAG, "handleDbError executed on thread: " + Thread.currentThread().getName());
    }
}
