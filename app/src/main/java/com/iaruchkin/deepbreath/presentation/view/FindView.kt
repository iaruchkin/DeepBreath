package com.iaruchkin.deepbreath.presentation.view

import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface FindView : MvpView {
    fun showCityList(cityList: List<Data?>)
    fun showFavorites(favorites: List<FavoritesEntity?>)
    fun showState(state: State)
}