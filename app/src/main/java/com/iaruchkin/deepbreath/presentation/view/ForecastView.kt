package com.iaruchkin.deepbreath.presentation.view

import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.ConditionEntity
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface ForecastView : MvpView {
    fun showWeather(forecastEntity: List<ForecastEntity>,
                    weatherEntity: List<WeatherEntity>,
                    conditionEntity: List<ConditionEntity>)

    fun showAqi(aqiEntity: List<AqiEntity>)
    fun showState(state: State)
    fun updateIcon(inFavorites: Boolean)
}