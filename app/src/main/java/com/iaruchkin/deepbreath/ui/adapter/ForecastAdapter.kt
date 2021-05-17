package com.iaruchkin.deepbreath.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.iaruchkin.deepbreath.App
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
    private val TAG_ADAPTER = "ADAPTER"
    private val mUseTodayLayout: Boolean

    private val context = App.INSTANCE.applicationContext
    private val forecastItemList = ArrayList<ForecastEntity>()
    private val conditionItemList = ArrayList<ConditionEntity>()

    private var weatherItem: WeatherEntity = WeatherEntity()
    private var aqiItem: AqiEntity? = AqiEntity()
    private var mIsSearch: Boolean = false

    interface ForecastAdapterOnClickHandler {
        fun onClickList(forecastItem: ForecastEntity, weatherEntity: WeatherEntity, aqiEntity: AqiEntity, conditionEntity: ConditionEntity, viewType: Int)
    }

    init {
        mUseTodayLayout = App.INSTANCE.applicationContext.resources.getBoolean(R.bool.use_today_layout)
    }

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
        val forecastItem = forecastItemList[position]
        val conditionText: String?
        val aqi: Int
        val iconForecast: Int
        val iconToday: Int
        val viewType = getItemViewType(position)

        when (viewType) {
            VIEW_TYPE_TODAY -> {

                if (conditionItemList.size != 0) {
                    iconToday = conditionItemList[ConditionUtils.getConditionCode(weatherItem.conditionCode)].icon
                    if (weatherItem.isDay == 1) {
                        conditionText = conditionItemList[ConditionUtils.getConditionCode(weatherItem.conditionCode)].dayText
                    } else {
                        conditionText = conditionItemList[ConditionUtils.getConditionCode(weatherItem.conditionCode)].nightText
                    }
                    Log.i(TAG_ADAPTER, forecastItem.toString())
                    Log.i(TAG_ADAPTER, conditionText)
                } else {
                    conditionText = null
                    iconToday = 0
                }

                holder.bindFirst(forecastItem, weatherItem, aqiItem, conditionText, iconToday)
            }

            VIEW_TYPE_FUTURE_DAY -> {
                if (conditionItemList.size != 0) {
                    iconForecast = conditionItemList[ConditionUtils.getConditionCode(forecastItem.conditionCode)].icon
                    Log.i(TAG_ADAPTER, forecastItem.toString())
                    Log.i(TAG_ADAPTER, iconForecast.toString())
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
        return if (mUseTodayLayout && position == 0) {
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

    inner class WeatherViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        private var imageView: ImageView?
        private var weatherDescTextView: TextView?
        private var locationTextView: TextView?
        private var dateTextView: TextView?
        private var highTemperatureTextView: TextView?
        private var lowTemperatureTextView: TextView?
        private var aqiTextView: TextView?
        private var aqiDesc: TextView?
        private var aqiHead: TextView?
        private var aqiCard: CardView?
        private var weatherCard: CardView?
        private var recomendation: TextView?
        private var invalidData: TextView?
        private var mProgressBar: ProgressBar?

        constructor(view: View) : super(view) {
            imageView = view.findViewById(R.id.weather_icon)
            weatherDescTextView = view.findViewById(R.id.weather_description)
            locationTextView = view.findViewById<TextView>(R.id.location_desc)
            dateTextView = view.findViewById(R.id.date)
            highTemperatureTextView = view.findViewById(R.id.high_value)
            lowTemperatureTextView = view.findViewById(R.id.low_value)
            aqiTextView = view.findViewById(R.id.aqi_value)
            aqiDesc = view.findViewById(R.id.aqi_description)
            aqiHead = view.findViewById(R.id.aqi_head)
            aqiCard = view.findViewById(R.id.aqi_card)
            weatherCard = view.findViewById(R.id.today_card)
            recomendation = view.findViewById(R.id.recomendation)
            invalidData = view.findViewById(R.id.invalid_data_sign)
            mProgressBar = view.findViewById(R.id.progress_bar)
            view.setOnClickListener(this)
        }

        fun bindFirst(forecastItem: ForecastEntity, weatherItem: WeatherEntity, aqi: AqiEntity?, dayText: String?, icon: Int) {

            val highString = WeatherUtils.formatTemperature(context, weatherItem.temp_c)
            val lowString = WeatherUtils.formatTemperature(context, weatherItem.feelslike_c)

            imageView?.setImageResource(WeatherUtils.getLargeArtResource(icon, weatherItem.isDay))

            var location = forecastItem.locationName
            if (location.isEmpty()) {
                location = aqi?.cityName?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()?.get(0)
                        ?: "..."
            }

            locationTextView?.text = location

            weatherDescTextView?.text = dayText

            dateTextView?.text = String.format(Locale.getDefault(), context.resources.getString(R.string.today) + " %s",
                    StringUtils.formatDate((weatherItem.last_updated_epoch).toLong(), "HH:mm"))

            highTemperatureTextView?.text = highString
            lowTemperatureTextView?.text = lowString


            if (aqi?.aqi == null) {
                aqiCard?.setCardBackgroundColor(context.resources.getColor(AqiUtils.getColor(AppPreferences.lastKnownAqi(context))))
                showState(State.LoadingAqi)
            } else {
                AppPreferences.saveAqi(context, aqi.aqi)

                showState(State.HasData)
                aqiTextView?.text = aqi.aqi.toString()
                aqiDesc?.setText(AqiUtils.getPollutionLevel(aqi.aqi))
                aqiCard?.setCardBackgroundColor(context.resources.getColor(AqiUtils.getColor(aqi.aqi)))

                if (LocationUtils.locationIsValid(aqi.locationLat, aqi.locationLon, context) || mIsSearch) {
                    recomendation?.setText(AqiUtils.getRecomendation(aqi.aqi))
                    invalidData?.visibility = View.GONE
                } else {
                    recomendation?.setText(R.string.invalid_data)
                    invalidData?.visibility = View.VISIBLE
                }
            }
        }

        fun bindFuture(forecastItem: ForecastEntity, icon: Int) {

            val highString = WeatherUtils.formatTemperature(context, forecastItem.maxtemp_c)
            val lowString = WeatherUtils.formatTemperature(context, forecastItem.mintemp_c)

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
                weatherItem.conditionCode
            else
                forecastItem.conditionCode

            val conditionItem = conditionItemList[ConditionUtils.getConditionCode(code)]
            if (aqiItem?.aqi != null) mClickHandler.onClickList(forecastItem, weatherItem, aqiItem!!, conditionItem, itemViewType)
        }

        fun showState(state: State) {
            when (state) {
                State.LoadingAqi -> {
                    aqiTextView?.visibility = View.INVISIBLE
                    aqiDesc?.visibility = View.INVISIBLE
                    aqiHead?.visibility = View.INVISIBLE
                    mProgressBar?.visibility = View.VISIBLE
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
}