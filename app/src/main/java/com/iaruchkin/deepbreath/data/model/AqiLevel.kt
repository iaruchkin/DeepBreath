package com.iaruchkin.deepbreath.data.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.iaruchkin.deepbreath.R

sealed class AqiLevel(
    @StringRes val pollutionLevel: Int,
    @ColorRes val color: Int,
    @ColorRes val backgroundColor: Int,
    @StringRes val pollutionLevelDescription: Int,
    @StringRes val pollutionLevelRecommendation: Int,
) {

    data object Good : AqiLevel(
        R.string.pollution_good,
        R.color.good,
        R.color.good_transparent,
        R.string.good_full,
        R.string.good_recomendation,
    )

    data object Moderate : AqiLevel(
        R.string.pollution_moderate,
        R.color.moderate,
        R.color.moderate_transparent,
        R.string.moderate_full,
        R.string.moderate_recomendation,
    )

    data object PreUnhealthy : AqiLevel(
        R.string.pollution_pre_unhealthy,
        R.color.pre_unhealthy,
        R.color.pre_unhealthy_transparent,
        R.string.pre_unhealthy_full,
        R.string.pre_unhealthy_recomendation,
    )

    data object Unhealthy : AqiLevel(
        R.string.pollution_unhealthy,
        R.color.unhealthy,
        R.color.unhealthy_transparent,
        R.string.unhealthy_full,
        R.string.unhealthy_recomendation,
    )

    data object VeryUnhealthy : AqiLevel(
        R.string.pollution_very_unhealthy,
        R.color.very_unhealthy,
        R.color.very_unhealthy_transparent,
        R.string.very_unhealty_full,
        R.string.very_unhealty_recomendation,
        )

    data object Hazardous : AqiLevel(
        R.string.pollution_hazardous,
        R.color.hazardous,
        R.color.hazardous_transparent,
        R.string.hazardous_full,
        R.string.hazardous_recomendation,
    )

    companion object {
        fun getPollutionLevel(aqi: Int?) = when {
            aqi == null -> Moderate
            aqi in 0..50 -> Good
            aqi in 51..100 -> Moderate
            aqi in 101..150 -> PreUnhealthy
            aqi in 151..200 -> Unhealthy
            aqi in 201..300 -> VeryUnhealthy
            aqi > 300 -> Hazardous
            else -> Moderate
        }
    }
}