package com.iaruchkin.deepbreath.ui.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.AppPreferences
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.ConditionEntity
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import com.iaruchkin.deepbreath.utils.*
import java.util.*


const val VIEW_TYPE_TODAY = 0
const val VIEW_TYPE_FUTURE_DAY = 1

class ForecastAdapter(private val mClickHandler: ForecastAdapterOnClickHandler) : RecyclerView.Adapter<ForecastAdapter.WeatherViewHolder>() {

    private val DATE_FORMAT = "HH:mm, EEEE"

    private val forecastItemList: MutableList<ForecastEntity> = mutableListOf()
    private val conditionItemList: MutableList<ConditionEntity> = mutableListOf()

    private var weatherItem: WeatherEntity? = null
    private var aqiItem: AqiEntity? = null
    private var mIsSearch: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val layoutId: Int
        when (viewType) {
            VIEW_TYPE_TODAY -> {
                layoutId = R.layout.item_today_forecast
            }
            VIEW_TYPE_FUTURE_DAY -> {
                layoutId = R.layout.item_forecast
            }
            else -> throw IllegalArgumentException("Invalid view type, value of $viewType")
        }

        val weatherListView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return WeatherViewHolder(weatherListView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val forecastItem = forecastItemList?.get(position)
        val conditionText: String?
        val aqi: Int
        val iconForecast: Int
        val iconToday: Int
        val viewType = getItemViewType(position)

        when (viewType) {
            VIEW_TYPE_TODAY -> {

                if (conditionItemList.isNotEmpty()) {
                    iconToday = conditionItemList[ConditionUtils.getConditionCode(weatherItem!!.conditionCode)].icon
                    if (weatherItem?.isDay == 1) {
                        conditionText = conditionItemList[ConditionUtils.getConditionCode(weatherItem!!.conditionCode)].dayText
                    } else {
                        conditionText = conditionItemList[ConditionUtils.getConditionCode(weatherItem!!.conditionCode)].nightText
                    }
                } else {
                    conditionText = null
                    iconToday = 0
                }

                holder.bindFirst(forecastItem, weatherItem!!, aqiItem, conditionText, iconToday)
            }

            VIEW_TYPE_FUTURE_DAY -> {
                if (conditionItemList.isNotEmpty()) {
                    iconForecast = conditionItemList[ConditionUtils.getConditionCode(forecastItem.conditionCode)].icon
                } else {
                    iconForecast = 0
                }

                holder.bindFuture(forecastItem, iconForecast)
            }

            else -> throw IllegalArgumentException("Invalid view type, value of $viewType")
        }
    }

    override fun getItemCount(): Int {
        return forecastItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_TODAY
        } else {
            VIEW_TYPE_FUTURE_DAY
        }
    }

    fun setWeather(forecastEntity: List<ForecastEntity>,
                   weatherEntity: WeatherEntity,
                   conditionEntity: List<ConditionEntity>) {
        forecastItemList.clear()
        forecastItemList.add(forecastEntity[0])
        forecastItemList.addAll(forecastEntity)

        weatherItem = weatherEntity

        conditionItemList.clear()
        conditionItemList.addAll(conditionEntity)

        notifyDataSetChanged()
    }

    fun setAqi(aqiEntity: AqiEntity, isSearch: Boolean) {
        aqiItem = aqiEntity
        notifyDataSetChanged()

        mIsSearch = isSearch
    }

    inner class WeatherViewHolder(var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var imageView: ImageView? = view.findViewById(R.id.weather_icon)
        private var weatherDescTextView: TextView? = view.findViewById(R.id.weather_description)
        private var locationTextView: TextView? = view.findViewById<TextView>(R.id.rLocationDesc)
        private var dateTextView: TextView? = view.findViewById(R.id.date)
        private var highTemperatureTextView: TextView? = view.findViewById(R.id.high_value)
        private var lowTemperatureTextView: TextView? = view.findViewById(R.id.low_value)
        private var aqiTextView: TextView? = view.findViewById(R.id.aqi_value)
        private var aqiDesc: TextView? = view.findViewById(R.id.aqi_description)
        private var aqiHead: TextView? = view.findViewById(R.id.aqi_head)
        private var aqiCard: CardView? = view.findViewById(R.id.aqi_card)
        private var weatherCard: CardView? = view.findViewById(R.id.today_card)
        private var recomendation: TextView? = view.findViewById(R.id.recomendation)
        private var invalidData: TextView? = view.findViewById(R.id.invalid_data_sign)
        private var mProgressBar: ProgressBar? = view.findViewById(R.id.progress_bar)

        init {
            view.setOnClickListener(this)
        }

        fun bindFirst(forecastItem: ForecastEntity, weatherItem: WeatherEntity, aqi: AqiEntity?, dayText: String?, icon: Int) {
            val highString = WeatherUtils.formatTemperature(view.context, weatherItem.temp_c)
            val lowString = WeatherUtils.formatTemperature(view.context, weatherItem.feelslike_c)

            imageView?.setImageResource(WeatherUtils.getLargeArtResource(icon, weatherItem.isDay))

            var location = forecastItem.locationName
            if (location.isEmpty()) {
                location = aqi?.cityName?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.get(0)
                        ?: "..."
            }

            locationTextView?.text = location

            weatherDescTextView?.text = dayText

            dateTextView?.text = String.format(Locale.getDefault(), view.context.resources.getString(R.string.today) + " %s",
                    StringUtils.formatDate((weatherItem.last_updated_epoch).toLong(), "HH:mm"))

            highTemperatureTextView?.text = highString
            lowTemperatureTextView?.text = lowString


            if (aqi?.aqi == null) {
                aqiCard?.setCardBackgroundColor(view.context.resources.getColor(AqiUtils.getColor(AppPreferences.lastKnownAqi(view.context))))
                showState(State.LoadingAqi)
            } else {
                AppPreferences.saveAqi(view.context, aqi.aqi)

                showState(State.HasData)
                aqiTextView?.text = aqi.aqi.toString()
                aqiDesc?.setText(AqiUtils.getPollutionLevel(aqi.aqi))
                aqiCard?.setCardBackgroundColor(view.context.resources.getColor(AqiUtils.getColor(aqi.aqi)))

                val isValid = if (!mIsSearch) {
                    LocationUtils.locationIsValid(aqi.getCoordinates(), view.context)
                } else {
                    val forecastLocation = Location("forecastItem Location").apply {
                            latitude = forecastItem.locationLat
                            longitude = forecastItem.locationLon
                        }
                    LocationUtils.locationIsValid(aqi.getCoordinates(), forecastLocation)
                }

                if (LocationUtils.locationIsValid(aqi.getCoordinates(), view.context) || mIsSearch) {
                    recomendation?.setText(AqiUtils.getRecomendation(aqi.aqi))
                    invalidData?.visibility = View.GONE
                } else {
                    recomendation?.setText(R.string.invalid_data)
                    invalidData?.visibility = View.VISIBLE
                }
            }
        }

        fun bindFuture(forecastItem: ForecastEntity, icon: Int) {

            val highString = WeatherUtils.formatTemperature(view.context, forecastItem.maxtemp_c)
            val lowString = WeatherUtils.formatTemperature(view.context, forecastItem.mintemp_c)

            imageView?.setImageResource(WeatherUtils.getSmallArtResource(icon, forecastItem.isDay))

            dateTextView?.text = StringUtils.formatDate(forecastItem.date_epoch.toLong(), "EEEE")
            weatherDescTextView?.text = StringUtils.formatDate(forecastItem.date_epoch.toLong(), "d MMMM")

            highTemperatureTextView?.text = highString
            lowTemperatureTextView?.text = lowString

        }

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            val forecastItem = forecastItemList[adapterPosition]
            val code = if (adapterPosition == 0)
                weatherItem!!.conditionCode
            else
                forecastItem.conditionCode

            val conditionItem = conditionItemList[ConditionUtils.getConditionCode(code)]
            if (aqiItem?.aqi != null) mClickHandler.onClickList(forecastItem, weatherItem!!, aqiItem!!, conditionItem, itemViewType)
        }

        fun showState(state: State) {
            when (state) {
                State.LoadingAqi -> {
                    mProgressBar?.visibility = View.VISIBLE
                    aqiTextView?.visibility = View.INVISIBLE
                    aqiDesc?.visibility = View.INVISIBLE
                    aqiHead?.visibility = View.INVISIBLE
                }
                State.HasData -> {
                    mProgressBar?.visibility = View.GONE
                    aqiTextView?.visibility = View.VISIBLE
                    aqiDesc?.visibility = View.VISIBLE
                    aqiHead?.visibility = View.VISIBLE
                }
                else -> throw IllegalArgumentException("Unknown state: $state")
            }
        }
    }

    interface ForecastAdapterOnClickHandler {
        fun onClickList(forecastItem: ForecastEntity, weatherEntity: WeatherEntity, aqiEntity: AqiEntity, conditionEntity: ConditionEntity, viewType: Int)
    }

}