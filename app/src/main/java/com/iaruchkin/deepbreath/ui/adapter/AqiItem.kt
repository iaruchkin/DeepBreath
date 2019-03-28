package com.iaruchkin.deepbreath.ui.adapter

import androidx.annotation.ColorInt
import com.iaruchkin.deepbreath.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_fancy.*
import kotlinx.android.synthetic.main.weather_detail_card.*


class AqiItem(private val number: Double, private val name: String) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.high_value.text = number.toString()
        viewHolder.value_header.text = name
    }

    override fun getLayout() = R.layout.weather_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}