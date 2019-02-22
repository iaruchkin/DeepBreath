package com.iaruchkin.deepbreath.presentation.presenter;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.common.BasePresenter;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.network.AqiResponse;
import com.iaruchkin.deepbreath.network.NetworkSilngleton;
import com.iaruchkin.deepbreath.presentation.view.WeatherListView;
import com.iaruchkin.deepbreath.room.ConverterNews;
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
    private NetworkSilngleton restApi;
    private final String DEFAULT_LOCATION = "here";
    public WeatherListPresenter(@NonNull NetworkSilngleton instance) {
        this.restApi = instance;
    }

    @Override
    protected void onFirstViewAttach() {
        loadFromDb(DEFAULT_LOCATION);
    }

    public void loadData(String location){
//        loadFromDb(location);
        loadDummy();

    }

    public void forceLoadData(String location){
//        loadFromNet(location);
        loadDummy();
    }

    private void loadDummy(){
        List<WeatherEntity> newsEntities = new ArrayList<>();
        WeatherEntity dummy = new WeatherEntity();
        dummy.setLocation("here");
        dummy.setId("1qwe");
        dummy.setDate("02/03/2019");
        dummy.setAqi("351");

        newsEntities.add(dummy);
        newsEntities.add(dummy);

        getViewState().showData(newsEntities);
    }

    private void loadFromDb(String location){
        getViewState().showState(State.Loading);
        Disposable loadFromDb = Single.fromCallable(() -> ConverterNews
                .loadDataFromDb(context, location))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> updateData(news, location), this::handleError);
        disposeOnDestroy(loadFromDb);
    }

    private void updateData(@Nullable List<WeatherEntity> data, String location) {
        if (data.size()==0){
            loadFromNet(location);
            Log.i(WEATHER_LIST_TAG, "there is no data in location : " + location);
        }else {
            getViewState().showData(data);
            getViewState().showState(State.HasData);
            Log.i(WEATHER_LIST_TAG, "loaded from DB: " + data.get(0).getLocation() + " / " + data.get(0).getAqi());
            Log.i(WEATHER_LIST_TAG, "updateData executed on thread: " + Thread.currentThread().getName());
        }
    }

    private void loadFromNet(@NonNull String location){
        getViewState().showState(State.Loading);
        final Disposable disposable = NetworkSilngleton.getInstance()
                .airQuality()
                .get(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> updateDB(response, location), this::handleError);
        disposeOnDestroy(disposable);
    }

    private void updateDB(AqiResponse response , String location) {
        if (response.getData()!=null) {
            getViewState().showState(State.HasNoData);
        } else {
            Disposable saveNewsToDb = Single.fromCallable(response::getData)
                    .subscribeOn(Schedulers.io())
                    .map(aqiDTO -> {
                        ConverterNews.saveAllDataToDb(context,
                                ConverterNews.dtoToDao(aqiDTO, location),location);
                        return ConverterNews.loadDataFromDb(context, location);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            weatherEntities -> {
                                getViewState().showData(weatherEntities);
                                Log.i(WEATHER_LIST_TAG, "loaded from NET to DB: " + weatherEntities.get(0).getLocation() + " / " + weatherEntities.get(0).getAqi());
                            });
            disposeOnDestroy(saveNewsToDb);
            getViewState().showState(State.HasData);
        }
    }

    private void handleError(Throwable th) {
        getViewState().showState(State.NetworkError);
        Log.e(WEATHER_LIST_TAG, th.getMessage(), th);
        Log.e(WEATHER_LIST_TAG, "handleError executed on thread: " + Thread.currentThread().getName());
    }
}
