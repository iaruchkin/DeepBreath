package com.iaruchkin.deepbreath.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.iaruchkin.deepbreath.common.State;
import com.iaruchkin.deepbreath.room.AqiEntity;
import com.iaruchkin.deepbreath.room.WeatherEntity;

import java.util.List;

import androidx.annotation.NonNull;

@StateStrategyType(value = SingleStateStrategy.class)
public interface WeatherListView extends MvpView {

    void showData(@NonNull List<WeatherEntity> data);
    void showState(@NonNull State state);

}
