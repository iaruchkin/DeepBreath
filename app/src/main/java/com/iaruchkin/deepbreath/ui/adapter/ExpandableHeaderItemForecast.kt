package com.iaruchkin.deepbreath.ui.adapter

import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.room.ConditionEntity
import com.iaruchkin.deepbreath.room.ForecastEntity
import com.iaruchkin.deepbreath.room.WeatherEntity
import com.iaruchkin.deepbreath.utils.ConditionUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import com.iaruchkin.deepbreath.utils.WeatherUtils
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_header_weather.*
import java.lang.String.valueOf
import java.util.*

class ExpandableHeaderItemForecast(private val forecastEntity: ForecastEntity, private val condition: ConditionEntity)
    : Item(), ExpandableItem{

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {

        if (forecastEntity.getIsDay() == 1) {
                viewHolder.weather_description.text = condition.dayText
        } else {
                viewHolder.weather_description.text = condition.nightText
        }

        viewHolder.high_value.text = valueOf(forecastEntity.maxtemp_c)
        viewHolder.low_value.text = valueOf(forecastEntity.mintemp_c)

        var format = "%s, %s"
        if(forecastEntity.locationRegion != "") format = "%s, %s, %s"
        viewHolder.location_desc.text = String.format(Locale.getDefault(), format, forecastEntity.locationCountry, forecastEntity.locationName, forecastEntity.locationRegion)

        viewHolder.weather_icon.setImageResource(WeatherUtils.getLargeArtResource(condition.icon, forecastEntity.getIsDay()))

        viewHolder.weather_date.setText(StringUtils.formatDate(forecastEntity.date_epoch*1L, "d MMMM, EEEE"))

        viewHolder.feels_like_detail.setText(R.string.night_feels_like)
        
        viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())
        viewHolder.item_expandable_header_title.setText(getRotatedTextResId())

        viewHolder.weather_expandable_header_root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())
            viewHolder.item_expandable_header_title.setText(getRotatedTextResId())
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

    private fun getRotatedTextResId() =
            if (expandableGroup.isExpanded)
                R.string.expandaple_less
            else
                R.string.expandaple_more
}
