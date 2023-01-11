package com.iaruchkin.deepbreath.screens.adapter

import android.view.View
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.utils.WeatherUtils
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.weather_detail_card.*

class WeatherItem(private val number: String, private val name: Int) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {

        if(name == R.string.sunrise ||
                name == R.string.sunset ||
                name == R.string.moonrise ||
                name == R.string.moonset ||
                name == R.string.wind_direction_label)
        {
            viewHolder.dimention_weather.visibility = View.GONE
        }
        else {
            viewHolder.dimention_weather.visibility = View.VISIBLE
        }

        if(name == R.string.pressure_label){
            viewHolder.weather_icon.visibility = View.GONE
        } else {
            viewHolder.weather_icon.visibility = View.VISIBLE
        }

        viewHolder.high_value.text = number
        viewHolder.value_header.setText(name)

        viewHolder.weather_icon.setImageResource(WeatherUtils.getWeatherDetailIcon(name))
        viewHolder.dimention_weather.setText(WeatherUtils.getWeatherUnit(App.INSTANCE.applicationContext, name))
    }

    override fun getLayout() = R.layout.weather_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}