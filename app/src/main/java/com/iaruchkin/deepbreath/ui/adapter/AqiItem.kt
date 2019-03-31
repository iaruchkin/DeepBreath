package com.iaruchkin.deepbreath.ui.adapter

import com.iaruchkin.deepbreath.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.weather_detail_card.*


class AqiItem(private val number: String, private val name: Int) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.high_value.text = number
        viewHolder.value_header.setText(name)
    }

    override fun getLayout() = R.layout.aqi_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}