package com.iaruchkin.deepbreath.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import androidx.annotation.NonNull;

@StateStrategyType(value = SingleStateStrategy.class)
public interface DetailView extends MvpView {

    void showTodayData(@NonNull WeatherEntity weatherEntity, @NonNull AqiEntity aqiEntity, @NonNull ConditionEntity condition);
    void showForecastData(@NonNull ForecastEntity data, @NonNull ConditionEntity condition);
}
