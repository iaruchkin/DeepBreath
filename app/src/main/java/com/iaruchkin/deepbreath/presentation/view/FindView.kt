package com.iaruchkin.deepbreath.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data

@StateStrategyType(value = SingleStateStrategy::class)
interface FindView : MvpView {
    fun showCityList(cityList: List<Data?>)
}