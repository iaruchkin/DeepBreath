package com.iaruchkin.deepbreath.ui.adapter

import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.room.ConditionEntity
import com.iaruchkin.deepbreath.room.WeatherEntity
import com.iaruchkin.deepbreath.utils.StringUtils
import com.iaruchkin.deepbreath.utils.WeatherUtils
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_header_weather.*
import java.lang.String.valueOf
import java.util.*

class ExpandableHeaderItemWeather(private val weatherEntity: WeatherEntity, private val condition: ConditionEntity)
    : Item(), ExpandableItem{

    internal var context = App.INSTANCE.applicationContext
    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {

        if (weatherEntity.getIsDay() == 1) {
            viewHolder.weather_description.text = condition.dayText
        } else {
            viewHolder.weather_description.text = condition.nightText
        }

        val highString = WeatherUtils.formatTemperature(context, weatherEntity.getTemp_c())
        val lowString = WeatherUtils.formatTemperature(context, weatherEntity.getFeelslike_c())

//        viewHolder.high_value.text = String.format(Locale.getDefault(), "%s°", weatherEntity.temp_c)
//        viewHolder.low_value.text = String.format(Locale.getDefault(), "%s°", weatherEntity.feelslike_c)

        viewHolder.high_value.text = highString
        viewHolder.low_value.text = lowString

        var format = "%s"
        if(weatherEntity.region != "") format = "%s, %s"
        viewHolder.location_desc.text = String.format(Locale.getDefault(), format
                , StringUtils.transliterateLatToRus(weatherEntity.location, weatherEntity.country)
                , StringUtils.transliterateLatToRus(weatherEntity.region, weatherEntity.country))

        viewHolder.weather_icon.setImageResource(WeatherUtils.getLargeArtResource(condition.icon, weatherEntity.getIsDay()))

        viewHolder.weather_date.setText(StringUtils.formatDate(weatherEntity.last_updated_epoch*1L, "HH:mm, EEEE"))

        viewHolder.feels_like_detail.setText(R.string.feels_like)

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
