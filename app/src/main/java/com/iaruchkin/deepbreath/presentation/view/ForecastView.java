package com.iaruchkin.deepbreath.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import java.util.List;

import androidx.annotation.NonNull;

@StateStrategyType(value = AddToEndSingleStrategy.class)
public interface ForecastView extends MvpView {

    void showData(@NonNull List<ForecastEntity> forecastEntity,
                         @NonNull List<WeatherEntity> weatherEntity,
                         @NonNull List<AqiEntity> aqiEntity,
                         @NonNull List<ConditionEntity> conditionEntity);
    void showWeather(@NonNull List<ForecastEntity> forecastEntity,
                  @NonNull List<WeatherEntity> weatherEntity,
                  @NonNull List<ConditionEntity> conditionEntity);
    void showAqi(@NonNull List<AqiEntity> aqiEntity);
    void showState(@NonNull State state);
}
