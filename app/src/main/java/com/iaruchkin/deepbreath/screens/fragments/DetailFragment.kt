package com.iaruchkin.deepbreath.screens.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.presentation.presenter.DetailPresenter
import com.iaruchkin.deepbreath.presentation.view.DetailView
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.ConditionEntity
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import com.iaruchkin.deepbreath.screens.ABOUT_TAG
import com.iaruchkin.deepbreath.screens.SETTINGS_TAG
import com.iaruchkin.deepbreath.screens.adapter.*
import com.iaruchkin.deepbreath.utils.LocationUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import com.iaruchkin.deepbreath.utils.WeatherUtils
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_details.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class DetailFragment : MvpAppCompatFragment(), DetailView {

    private val LAYOUT = R.layout.fragment_details

    internal var context = App.INSTANCE.applicationContext
    private val compositeDisposable = CompositeDisposable()
    private var listener: MessageFragmentListener? = null

    internal lateinit var weather: WeatherEntity
    internal lateinit var forecast: ForecastEntity
    internal lateinit var aqi: AqiEntity

    private var toolbar: Toolbar? = null

    @JvmField
    @InjectPresenter
    var detailPresenter: DetailPresenter? = null

    @ProvidePresenter
    fun providePresenter(): DetailPresenter {
        val idForecast = requireArguments().getString(FORECAST_ID, "")
        val idWeather = requireArguments().getString(WEATHER_ID, "")
        val idAqi = requireArguments().getString(AQI_ID, "")
        val idCondition = requireArguments().getString(CONDITION_ID, "")
        val viewType = requireArguments().getInt(VIEW_TYPE, 1)
        return DetailPresenter(idForecast, idWeather, idAqi, idCondition, viewType)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(LAYOUT, container, false)

        toolbar = view.findViewById(R.id.rToolbar)

        setupToolbar()

//        MaterialAlertDialogBuilder(activity, R.style.AppTheme)
//                .setTitle("Title")
//                .setMessage("Message")
//                .setPositiveButton("Ok", null)
//                .show()

        return view
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    private fun generateWeather(data: WeatherEntity): MutableList<WeatherItem> {

        weather = data

        val wind = data.wind_kph
        val pressureMb = data.pressure_mb
        val precipMm = data.precip_mm
        val humidity = data.humidity
        val windDir = data.wind_dir

        val dataList: MutableList<WeatherItem> = mutableListOf()

        dataList.add(WeatherItem(WeatherUtils.formatWind(context, wind), R.string.wind_label))
        dataList.add(WeatherItem(getString(WeatherUtils.getWindDirection(windDir)), R.string.wind_direction_label))
        dataList.add(WeatherItem(WeatherUtils.formatPressure(context, pressureMb), R.string.pressure_label))
//        dataList.add(WeatherItem(precipMm.toString(), R.string.precipitation))
        dataList.add(WeatherItem(humidity.toString(), R.string.humidity_label))

        return dataList
    }

    private fun generateAqi(data: AqiEntity): MutableList<AqiItem> {

        aqi = data

        val pm25 = data.pm25
        val pm10 = data.pm10
        val co = data.co
        val no2 = data.no2
        val so2 = data.so2
        val w = null
        val wg = null
        val h = null
        val p = null
        val o3 = data.o3

        val dataList: MutableList<AqiItem> = mutableListOf()

        if (pm25 != null) {
            dataList.add(AqiItem(pm25.toString(), R.string.pm25, R.string.pm25_header, R.string.pm25_description, activity))
        } else {
            dataList.add(AqiItem(data.aqi.toString(), R.string.pm25, R.string.pm25_header, R.string.pm25_description, activity))
        }//todo fix later

        if (pm10 != null) dataList.add(AqiItem(pm10.toString(), R.string.pm10, R.string.pm10_header, R.string.pm10_description, activity))
        if (co != null) dataList.add(AqiItem(co.toString(), R.string.co, R.string.co_header, R.string.co_description, activity))
        if (no2 != null) dataList.add(AqiItem(no2.toString(), R.string.no2, R.string.no2_header, R.string.no2_description, activity))
        if (so2 != null) dataList.add(AqiItem(so2.toString(), R.string.so2, R.string.so2_header, R.string.so2_description, activity))
        if (w != null) dataList.add(AqiItem(w.toString(), R.string.w, R.string.w_header, R.string.w_description, activity))
        if (wg != null) dataList.add(AqiItem(wg.toString(), R.string.wg, R.string.wg_header, R.string.wg_description, activity))
        if (p != null) dataList.add(AqiItem(p.toString(), R.string.p, R.string.p_header, R.string.p_description, activity))
        if (h != null) dataList.add(AqiItem(h.toString(), R.string.h, R.string.h_header, R.string.h_description, activity))
        if (o3 != null) dataList.add(AqiItem(o3.toString(), R.string.o3, R.string.o3_header, R.string.o3_description, activity))

        return dataList
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun generateForecast(data: ForecastEntity): MutableList<WeatherItem> {

        forecast = data

        val wind = data.maxwind_kph
        val windDir = data.wind_degree
        val precipMm = data.totalprecip_mm
        val moonrise = StringUtils.formatTime(data.moonrise, "HH:mm")
        val moonset = StringUtils.formatTime(data.moonset, "HH:mm")
        val sunset = StringUtils.formatTime(data.sunset, "HH:mm")
        val sunrise = StringUtils.formatTime(data.sunrise, "HH:mm")

        val dataList: MutableList<WeatherItem> = mutableListOf()

        dataList.add(WeatherItem(WeatherUtils.formatWind(context, wind), R.string.wind_label))
        dataList.add(WeatherItem(getString(WeatherUtils.getWindDirection(windDir.toString())), R.string.wind_direction_label))//todo fix wint utils

//        dataList.add(WeatherItem(formatPrecip(context, precipMm), R.string.precipitation))
//        dataList.add(WeatherItem(sunrise, R.string.sunrise))
//        dataList.add(WeatherItem(sunset, R.string.sunset))
//        dataList.add(WeatherItem(moonrise, R.string.moonrise))
//        dataList.add(WeatherItem(moonset, R.string.moonset))

        return dataList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            if (listener != null) {
                listener!!.onActionClicked(SETTINGS_TAG)
            }
            true
        }
        R.id.action_about -> {
            if (listener != null) {
                listener!!.onActionClicked(ABOUT_TAG)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupFirst(weatherEntity: WeatherEntity, aqiEntity: AqiEntity, condition: ConditionEntity) {

        val weatherItems = generateWeather(weatherEntity)
        val aqiItems = generateAqi(aqiEntity)

        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = 2
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }
            adapter = groupAdapter
        }

        val city = if (LocationUtils.locationIsValid(aqiEntity.getCoordinates(), context)) aqiEntity.cityName else weatherEntity.location

        ExpandableGroup(ExpandableHeaderItemAqi(aqiEntity, city), false).apply {
            add(Section(aqiItems))
            groupAdapter.add(this)
        }


        ExpandableGroup(StripeItem(), false).apply {
            groupAdapter.add(this)
        }

        ExpandableGroup(ExpandableHeaderItemWeather(weatherEntity, condition), false).apply {
            add(Section(weatherItems))
            groupAdapter.add(this)
        }
    }

    private fun setupForecast(forecastEntity: ForecastEntity, condition: ConditionEntity) {

        val boringFancyItems = generateForecast(forecastEntity)

        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = 2
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }
            adapter = groupAdapter
        }

        ExpandableGroup(ExpandableHeaderItemForecast(forecastEntity, condition), true).apply {
            add(Section(boringFancyItems))
            groupAdapter.add(this)
        }
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (getContext() as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (getContext() as AppCompatActivity).supportActionBar

        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(true)
        actionBar.title = getString(R.string.title_activity_detail)
    }

    override fun showTodayData(weatherEntity: WeatherEntity, aqiEntity: AqiEntity, condition: ConditionEntity) {
        setupFirst(weatherEntity, aqiEntity, condition)
    }

    override fun showForecastData(forecastEntity: ForecastEntity, condition: ConditionEntity) {
        setupForecast(forecastEntity, condition)
    }

    companion object {

        internal const val FORECAST_ID = "extra:forecast"
        internal const val WEATHER_ID = "extra:weather"
        internal const val AQI_ID = "extra:aqi"
        internal const val CONDITION_ID = "extra:condition"
        internal const val VIEW_TYPE = "extra:viewType"

        @JvmStatic
        fun newInstance(idForecast: String, idWeather: String, idAqi: String, idCondition: String, viewType: Int): DetailFragment {
            val fragmentAqi = DetailFragment()
            val bundle = Bundle()
            bundle.putString(FORECAST_ID, idForecast)
            bundle.putString(WEATHER_ID, idWeather)
            bundle.putString(AQI_ID, idAqi)
            bundle.putString(CONDITION_ID, idCondition)
            bundle.putInt(VIEW_TYPE, viewType)

            fragmentAqi.arguments = bundle
            return fragmentAqi
        }
    }
}
