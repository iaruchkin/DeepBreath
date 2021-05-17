package com.iaruchkin.deepbreath.presentation.view

import com.iaruchkin.deepbreath.network.dtos.findCityDTO.Data
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SingleStateStrategy::class)
interface FindView : MvpView {
    fun showCityList(cityList: List<Data?>)
    fun showFavorites(favorites: List<FavoritesEntity?>)
}