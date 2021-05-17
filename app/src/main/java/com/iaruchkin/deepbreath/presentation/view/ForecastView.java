package com.iaruchkin.deepbreath.presentation.view;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ForecastView extends MvpView {

    void showWeather(@NonNull List<ForecastEntity> forecastEntity,
                  @NonNull List<WeatherEntity> weatherEntity,
                  @NonNull List<ConditionEntity> conditionEntity);
    void showAqi(@NonNull List<AqiEntity> aqiEntity);
    void showState(@NonNull State state);
}
