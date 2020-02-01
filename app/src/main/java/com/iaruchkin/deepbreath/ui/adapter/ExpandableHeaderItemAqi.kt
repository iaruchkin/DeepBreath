package com.iaruchkin.deepbreath.ui.adapter

import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.utils.AqiUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.aqi_card_layout.*
import kotlinx.android.synthetic.main.expandable_header_aqi.*
import java.lang.String.valueOf


class ExpandableHeaderItemAqi(private val aqiEntity: AqiEntity, val city: String)
    : Item(), ExpandableItem, View.OnTouchListener{
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v!!.parent.requestDisallowInterceptTouchEvent(true)
        return false
    }

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {

        //setting color to imported card
        viewHolder.aqi_exp_card_imported.setBackgroundResource(AqiUtils.getColor(aqiEntity.aqi))

        //setting full description and making it scrollable inside recyclerview
        viewHolder.aqi_level_full.setText(AqiUtils.getPollutionLevelFull(aqiEntity.aqi))
        viewHolder.aqi_level_full.setOnTouchListener(this)
        viewHolder.aqi_level_full.movementMethod = ScrollingMovementMethod.getInstance()

        viewHolder.aqi_location_desc.text = city
        viewHolder.aqi_value.text = valueOf(aqiEntity.aqi)
        viewHolder.aqi_description.setText(AqiUtils.getPollutionLevel(aqiEntity.aqi))
        viewHolder.aqi_date_exp.setText(StringUtils.formatDateAqi(aqiEntity.dateEpoch * 1L, "HH:mm, EEEE"))

        viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())
        viewHolder.item_expandable_header_title.setText(getRotatedTextResId())

        viewHolder.aqi_expandable_header_root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())
            viewHolder.item_expandable_header_title.setText(getRotatedTextResId())
        }
    }

    override fun getLayout() = R.layout.expandable_header_aqi

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getRotatedIconResId() =
            if (expandableGroup.isExpanded)
                R.drawable.ic_keyboard_arrow_down_black_24dp
            else
                R.drawable.ic_keyboard_arrow_right_black_24dp

    private fun getRotatedTextResId() =
            if (expandableGroup.isExpanded)
                R.string.expandaple_less
            else
                R.string.expandaple_more
}