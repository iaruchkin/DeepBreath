package com.iaruchkin.deepbreath.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment
import com.iaruchkin.deepbreath.presentation.presenter.DetailPresenter
import com.iaruchkin.deepbreath.presentation.view.DetailView
import com.iaruchkin.deepbreath.room.*
import com.iaruchkin.deepbreath.ui.MainActivity.SETTINGS_TAG
import com.iaruchkin.deepbreath.ui.adapter.*
import com.iaruchkin.deepbreath.utils.StringUtils
import com.iaruchkin.deepbreath.utils.WeatherUtils
import com.iaruchkin.deepbreath.utils.WeatherUtils.formatPrecip
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.fragment_details.*

class GroupieFragment : MvpAppCompatFragment(), DetailView {

    private val LAYOUT = R.layout.fragment_details

    internal var context = App.INSTANCE.getApplicationContext()

    private val compositeDisposable = CompositeDisposable()
    val TAG = GroupieFragment::class.java.getSimpleName()
    private var listener: MessageFragmentListener? = null

    internal lateinit var weather: WeatherEntity
    internal lateinit var forecast: ForecastEntity
    internal lateinit var aqi: AqiEntity

    private var toolbar: Toolbar? = null

    @JvmField
    @InjectPresenter
    var detailPresenter: DetailPresenter? = null

    @ProvidePresenter
    internal fun providePresenter(): DetailPresenter {
        val idForecast = if (arguments != null) arguments!!.getString(FORECAST_ID, "") else null
        val idWeather = if (arguments != null) arguments!!.getString(WEATHER_ID, "") else null
        val idAqi = if (arguments != null) arguments!!.getString(AQI_ID, "") else null
        val idCondition = if (arguments != null) arguments!!.getString(CONDITION_ID, "") else null
        val viewType = if (arguments != null) arguments!!.getInt(VIEW_TYPE, 1) else 0
        return DetailPresenter(idForecast, idWeather, idAqi, idCondition, viewType)
    }

    companion object {

        internal val FORECAST_ID = "extra:forecast"
        internal val WEATHER_ID = "extra:weather"
        internal val AQI_ID = "extra:aqi"
        internal val CONDITION_ID = "extra:condition"
        internal val VIEW_TYPE = "extra:viewType"

        @JvmStatic
        fun newInstance(idForecast: String, idWeather: String, idAqi: String, idCondition: String, viewType: Int): GroupieFragment {
            val fragmentAqi = GroupieFragment()
            val bundle = Bundle()
            bundle.putString(FORECAST_ID, idForecast)
            bundle.putString(WEATHER_ID, idWeather)
            bundle.putString(AQI_ID, idAqi)
            bundle.putString(CONDITION_ID, idCondition)
            bundle.putInt(VIEW_TYPE, viewType)

            fragmentAqi.setArguments(bundle)
            return fragmentAqi
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(LAYOUT, container, false)

        toolbar = view.findViewById(R.id.toolbar)

        setupToolbar()

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

    private fun generateWeather(data: WeatherEntity): MutableList<WeatherItem>{

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
        dataList.add(WeatherItem(precipMm.toString(), R.string.precipitation))
        dataList.add(WeatherItem(humidity.toString(), R.string.humidity_label))

        return dataList
    }

    private fun generateAqi(data: AqiEntity): MutableList<AqiItem>{

        aqi = data

        val pm25 = data.pm25
        val pm10 = data.pm10
        val co = data.co
        val no2 = data.no2
        val so2 = data.so2
        val w = data.w
        val wg = data.wg
        val h = data.h
        val p = data.p
        val o3 = data.o3

        val dataList: MutableList<AqiItem> = mutableListOf()

        if (pm25!=null) dataList.add(AqiItem(pm25.toString(), R.string.pm25))
        if (pm10!=null) dataList.add(AqiItem(pm10.toString(), R.string.pm10))
        if (co!=null) dataList.add(AqiItem(co.toString(), R.string.co))
        if (no2!=null) dataList.add(AqiItem(no2.toString(), R.string.no2))
        if (so2!=null) dataList.add(AqiItem(so2.toString(), R.string.so2))
        if (w!=null) dataList.add(AqiItem(w.toString(), R.string.w))
        if (wg!=null) dataList.add(AqiItem(wg.toString(), R.string.wg))
        if (p!=null) dataList.add(AqiItem(p.toString(), R.string.p))
        if (h!=null) dataList.add(AqiItem(h.toString(), R.string.h))
        if (o3!=null) dataList.add(AqiItem(o3.toString(), R.string.o3))

        return dataList
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun generateForecast(data: ForecastEntity): MutableList<WeatherItem>{

        forecast = data

        val wind = data.maxwind_kph
        val precipMm = data.totalprecip_mm
        val moonrise = StringUtils.formatTime(data.moonrise, "HH:mm")
        val moonset = StringUtils.formatTime(data.moonset, "HH:mm")
        val sunset = StringUtils.formatTime(data.sunset, "HH:mm")
        val sunrise = StringUtils.formatTime(data.sunrise, "HH:mm")

        val dataList: MutableList<WeatherItem> = mutableListOf()

        dataList.add(WeatherItem(WeatherUtils.formatWind(context, wind), R.string.wind_label))
        dataList.add(WeatherItem(formatPrecip(context, precipMm), R.string.precipitation))//todo precip formatter
        dataList.add(WeatherItem(sunrise, R.string.sunrise))
        dataList.add(WeatherItem(sunset, R.string.sunset))
        dataList.add(WeatherItem(moonrise, R.string.moonrise))
        dataList.add(WeatherItem(moonset, R.string.moonset))

        return dataList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                if (listener != null) {
                    listener!!.onActionClicked(SETTINGS_TAG)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setupFirst(weatherEntity : WeatherEntity, aqiEntity: AqiEntity, condition: ConditionEntity){

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

        ExpandableGroup(ExpandableHeaderItemAqi(aqiEntity), false).apply {
            add(Section(aqiItems))
            groupAdapter.add(this)
        }


        ExpandableGroup(StripeItem(), false).apply {
            groupAdapter.add(this) //todo это костыль, надо поправить
        }

        ExpandableGroup(ExpandableHeaderItemWeather(weatherEntity, condition), false).apply {
            add(Section(weatherItems))
            groupAdapter.add(this)
        }
    }

    private fun setupForecast(forecastEntity : ForecastEntity, condition: ConditionEntity){

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
        val actionBar = (getContext() as AppCompatActivity).getSupportActionBar()

        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowTitleEnabled(true)
        actionBar.setTitle(getString(R.string.title_activity_detail))
    }

    override fun showTodayData(weatherEntity: WeatherEntity, aqiEntity: AqiEntity, condition: ConditionEntity) {
        setupFirst(weatherEntity, aqiEntity, condition)
    }

    override fun showForecastData(forecastEntity: ForecastEntity, condition: ConditionEntity) {
        setupForecast(forecastEntity, condition)
    }
}
