package com.iaruchkin.deepbreath.ui.adapter

import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.weather_detail_card.*

class AqiItem(private val number: String, private val name: Int) : Item(), View.OnClickListener {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.high_value.text = number
        viewHolder.value_header.setText(name)

        viewHolder.itemView.setOnClickListener(this)
    }

    override fun getLayout() = R.layout.aqi_detail_card

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 2

    override fun onClick(v: View?) {
        action()
    }

    fun action() {
        MaterialAlertDialogBuilder(App.INSTANCE.applicationContext)
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Ok", null)
                .show()
    }
}