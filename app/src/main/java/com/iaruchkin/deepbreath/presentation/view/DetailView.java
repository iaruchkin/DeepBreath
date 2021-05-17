package com.iaruchkin.deepbreath.presentation.view;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = SingleStateStrategy.class)
public interface DetailView extends MvpView {

    void showTodayData(@NonNull WeatherEntity weatherEntity, @NonNull AqiEntity aqiEntity, @NonNull ConditionEntity condition);
    void showForecastData(@NonNull ForecastEntity data, @NonNull ConditionEntity condition);
}
