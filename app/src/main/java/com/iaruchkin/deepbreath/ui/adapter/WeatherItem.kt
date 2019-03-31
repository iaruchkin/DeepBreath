package com.iaruchkin.deepbreath.ui.adapter

import android.view.View
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.utils.WeatherUtils
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.weather_detail_card.*


class WeatherItem(private val number: String, private val name: Int) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {

        if(name == R.string.pressure) viewHolder.weather_icon.visibility = View.GONE
        else viewHolder.weather_icon.visibility = View.VISIBLE

        if(name == R.string.sunrise || name == R.string.sunset || name == R.string.moonrise || name == R.string.moonset) viewHolder.dimention_weather.visibility = View.GONE
        else viewHolder.weather_icon.visibility = View.VISIBLE

        viewHolder.high_value.text = number
        viewHolder.value_header.setText(name)

        viewHolder.weather_icon.setImageResource(WeatherUtils.getWeatherDetailIcon(name))
        viewHolder.dimention_weather.setText(WeatherUtils.getWeatherDimention(name))
    }

    override fun getLayout() = R.layout.weather_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}