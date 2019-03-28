package com.iaruchkin.deepbreath.ui.adapter

import android.view.View
import com.iaruchkin.deepbreath.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.weather_detail_card.*


class WeatherItem(private val number: String, private val name: String) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.high_value.text = number
        viewHolder.value_header.text = name
        viewHolder.weather_icon.visibility = View.GONE
    }

    override fun getLayout() = R.layout.weather_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}