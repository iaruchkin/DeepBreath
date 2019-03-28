package com.iaruchkin.deepbreath.ui.adapter

import com.iaruchkin.deepbreath.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_header_weather.*

//import kotlinx.android.synthetic.main.item_expandable_header.*


class ExpandableHeaderItemWeather(private val title: String)
    : Item(), ExpandableItem{

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.item_expandable_header_title.text = title
        viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())

        viewHolder.weather_expandable_header_root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())
        }
    }

    override fun getLayout() = R.layout.expandable_header_weather

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getRotatedIconResId() =
            if (expandableGroup.isExpanded)
                R.drawable.ic_keyboard_arrow_down_black_24dp
            else
                R.drawable.ic_keyboard_arrow_right_black_24dp

}