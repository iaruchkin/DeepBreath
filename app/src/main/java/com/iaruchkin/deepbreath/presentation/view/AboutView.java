package com.iaruchkin.deepbreath.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import androidx.annotation.NonNull;

@StateStrategyType(value = SingleStateStrategy.class)
public interface AboutView extends MvpView {

    void openURL(@NonNull String url);
    void composeEmail();

}


