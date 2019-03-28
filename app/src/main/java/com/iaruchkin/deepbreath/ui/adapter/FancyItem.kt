package com.iaruchkin.deepbreath.ui.adapter

import androidx.annotation.ColorInt
import com.iaruchkin.deepbreath.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_fancy.*
import kotlinx.android.synthetic.main.weather_detail_card.*


class FancyItem(@ColorInt private val color: Int, private val number: Int) : Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
//        viewHolder.item_fancy_cardView.setCardBackgroundColor(color)
//        viewHolder.item_fancy_number.text = number.toString()

        viewHolder.weather_detail_card.setCardBackgroundColor(color)
        viewHolder.high_value.text = number.toString()
    }

//    override fun getLayout() = R.layout.item_fancy
    override fun getLayout() = R.layout.weather_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2
}