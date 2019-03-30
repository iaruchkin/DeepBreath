package com.iaruchkin.deepbreath.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ConditionEntity;
import com.iaruchkin.deepbreath.room.ForecastEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.List;

import androidx.annotation.NonNull;

@StateStrategyType(value = SingleStateStrategy.class)
public interface DetailView extends MvpView {

//    void showWeatherData(@NonNull WeatherEntity data);
//    void showAqiData(@NonNull AqiEntity data);
//    void showState(@NonNull State state);

    void showTodayData(@NonNull WeatherEntity weatherEntity, @NonNull AqiEntity aqiEntity, @NonNull ConditionEntity condition);
    void showForecastData(@NonNull ForecastEntity data, @NonNull ConditionEntity condition);


}
