package com.iaruchkin.deepbreath.ui.adapter

import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.room.AqiEntity
import com.iaruchkin.deepbreath.utils.AqiUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.aqi_card.*
import kotlinx.android.synthetic.main.expandable_header_aqi.*
import java.lang.String.valueOf

class StripeItem(): Item(), ExpandableItem{

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout() = R.layout.stripe

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }
}