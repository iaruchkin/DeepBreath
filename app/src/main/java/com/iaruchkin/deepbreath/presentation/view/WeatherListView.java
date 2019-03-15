package com.iaruchkin.deepbreath.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.ForecastEntity;

import java.util.List;

import androidx.annotation.NonNull;

@StateStrategyType(value = SingleStateStrategy.class)
public interface WeatherListView extends MvpView {

    void showWeatherData(@NonNull List<ForecastEntity> data);
    void showAqiData(@NonNull List<AqiEntity> data);
    void showState(@NonNull State state);//todo replace with refresher
//    void showRefresher(@NonNull boolean show);

}
