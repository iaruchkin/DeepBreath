package com.iaruchkin.deepbreath.presentation.view;

import androidx.annotation.NonNull;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(value = SingleStateStrategy.class)
public interface AboutView extends MvpView {

    void openURL(@NonNull String url);
    void composeEmail();

}


