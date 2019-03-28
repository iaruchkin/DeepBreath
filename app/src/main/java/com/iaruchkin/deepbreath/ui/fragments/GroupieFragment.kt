package com.iaruchkin.deepbreath.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*

import androidx.recyclerview.widget.GridLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.presentation.presenter.AqiPresenter
import com.iaruchkin.deepbreath.presentation.view.AqiView
import com.iaruchkin.deepbreath.room.AqiEntity
import com.iaruchkin.deepbreath.room.ForecastEntity
import com.iaruchkin.deepbreath.room.WeatherEntity
import com.iaruchkin.deepbreath.ui.adapter.AqiItem
import com.iaruchkin.deepbreath.ui.adapter.ExpandableHeaderItemWeather
import com.iaruchkin.deepbreath.ui.adapter.ExpandableHeaderItemAqi
import com.iaruchkin.deepbreath.ui.adapter.WeatherItem
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.groupie_fragment.*

class GroupieFragment : MvpAppCompatFragment(), AqiView{

    //    private val LAYOUT = R.layout.layout_detail
    private val LAYOUT = R.layout.groupie_fragment

    internal var context = App.INSTANCE.getApplicationContext()

    private val compositeDisposable = CompositeDisposable()
    val TAG = GroupieFragment::class.java.getSimpleName()
    private var listener: MessageFragmentListener? = null

    internal lateinit var weather: WeatherEntity
    internal lateinit var forecast: ForecastEntity
    internal lateinit var aqi: AqiEntity


//    val boringFancyItems = generateWeather(7, 10)
//    val excitingFancyItems = generateWeather(6, 10)
//    val groupAdapter = GroupAdapter<ViewHolder>().apply {
//        spanCount = 2
//    }

//    var boringFancyItems : MutableList<WeatherItem>? = null

    @JvmField
    @InjectPresenter
    var aqiPresenter: AqiPresenter? = null

//    internal var date: TextView
//    internal var weather_description: TextView
//    internal var high_temperature: TextView
//    internal var low_temperature: TextView
//    internal var imageView: ImageView
//
//    internal var humidity: TextView
//    internal var pressure: TextView
//    internal var wind_measurement: TextView
//
//    internal var aqiDetails: View
//    internal var aqiLabel: TextView

    @ProvidePresenter
    internal fun providePresenter(): AqiPresenter {
        val idForecast = if (arguments != null) arguments!!.getString(FORECAST_ID, "") else null
        val idWeather = if (arguments != null) arguments!!.getString(WEATHER_ID, "") else null
        val idAqi = if (arguments != null) arguments!!.getString(AQI_ID, "") else null
        val viewType = if (arguments != null) arguments!!.getInt(VIEW_TYPE, 1) else 0
        return AqiPresenter(idForecast, idWeather, idAqi, viewType)
    }

    private val excitingSection = Section()

    companion object {

        internal val FORECAST_ID = "extra:forecast"
        internal val WEATHER_ID = "extra:weather"
        internal val AQI_ID = "extra:aqi"
        internal val VIEW_TYPE = "extra:viewType"

        @JvmStatic
        fun newInstance(idForecast: String, idWeather: String, idAqi: String, viewType: Int): GroupieFragment {
            val fragmentAqi = GroupieFragment()
            val bundle = Bundle()
            bundle.putString(FORECAST_ID, idForecast)
            bundle.putString(WEATHER_ID, idWeather)
            bundle.putString(AQI_ID, idAqi)
            bundle.putInt(VIEW_TYPE, viewType)

            fragmentAqi.setArguments(bundle)
            return fragmentAqi
        }
    }

    override fun  onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        retainInstance = true
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(LAYOUT, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
//        list_recycler_view.apply {
//            // set a LinearLayoutManager to handle Android
//            // RecyclerView behavior
//            layoutManager = LinearLayoutManager(activity)
//            // set the custom adapter to the RecyclerView
//            adapter = ListAdapter(mNicolasCageMovies)
//        }
//        recycler_view.apply {
//            layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
//                spanSizeLookup = groupAdapter.spanSizeLookup
//            }
//            adapter = groupAdapter
//        }

//        setupFirst()

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

        val wind = data.wind_kph
        val pressureMb = data.pressure_mb
        val precipMm = data.precip_mm
        val humidity = data.humidity
        val windDir = data.wind_dir

        val dataList: MutableList<WeatherItem> = mutableListOf()

        dataList.add(WeatherItem(wind.toString(), "wind"))
        dataList.add(WeatherItem(windDir, "wind direction"))
        dataList.add(WeatherItem(pressureMb.toString(), "pressure"))
        dataList.add(WeatherItem(precipMm.toString(), "precipitation"))
        dataList.add(WeatherItem(humidity.toString(), "humidity"))

        return dataList
    }

    private fun generateAqi(data: AqiEntity): MutableList<AqiItem>{

        val pm25 = data.pm25.toDouble()
        val pm10 = data.pm10.toDouble()
        val co = data.co
        val no2 = data.no2
        val so2 = data.so2
        val w = data.w
        val wg = data.wg
        val h = data.h
        val p = data.p
        val o3 = data.o3

        val dataList: MutableList<AqiItem> = mutableListOf()

        dataList.add(AqiItem(pm25, "pm25"))
        dataList.add(AqiItem(pm10, "pm10"))
        if (co!=null) dataList.add(AqiItem(co, "co"))
        if (no2!=null) dataList.add(AqiItem(no2, "no2"))
        if (so2!=null) dataList.add(AqiItem(so2, "so2"))
        if (w!=null) dataList.add(AqiItem(w, "w"))
        if (wg!=null) dataList.add(AqiItem(wg, "wg"))
        if (p!=null) dataList.add(AqiItem(p, "p"))
        if (h!=null) dataList.add(AqiItem(h, "h"))
        if (o3!=null) dataList.add(AqiItem(o3, "o3"))

        return dataList
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    private fun generateForecast(data: ForecastEntity): MutableList<WeatherItem>{

        val wind = data.maxwind_kph
        val precipMm = data.totalprecip_mm
        val moonrise = data.moonrise
        val moonset = data.moonset
        val sunset = data.sunset
        val sunrise = data.sunrise


        val dataList: MutableList<WeatherItem> = mutableListOf()

        dataList.add(WeatherItem(wind.toString(), "wind"))
        dataList.add(WeatherItem(precipMm.toString(), "precipitation"))
        dataList.add(WeatherItem(moonrise, "moonrise"))
        dataList.add(WeatherItem(moonset, "moonset"))
        dataList.add(WeatherItem(sunset, "sunset"))
        dataList.add(WeatherItem(sunrise, "sunrise"))


        return dataList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupFirst(weatherEntity : WeatherEntity, aqiEntity: AqiEntity){

        val boringFancyItems = generateWeather(weatherEntity)
        val excitingFancyItems = generateAqi(aqiEntity)

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
//            excitingSection.addAll(excitingFancyItems)
//            add(excitingSection)
            add(Section(excitingFancyItems))
            groupAdapter.add(this)
        }

        ExpandableGroup(ExpandableHeaderItemWeather("Weather weatherEntity"), false).apply {
            add(Section(boringFancyItems))
            groupAdapter.add(this)
        }

//        fab.setOnClickListener {
//            excitingFancyItems.shuffle()
//            excitingSection.update(excitingFancyItems)
//        }

    }

    private fun setupForecast(data : ForecastEntity){

        val boringFancyItems = generateForecast(data)

        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = 2
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }
            adapter = groupAdapter
        }

        ExpandableGroup(ExpandableHeaderItemWeather("Forecast data"), true).apply {
            add(Section(boringFancyItems))
            groupAdapter.add(this)
        }
    }


    override fun showWeatherData(data: WeatherEntity) {
    }

    override fun showAqiData(data: AqiEntity) {

    }

    override fun showState(state: State) {
        when (state) {
            State.Current -> {
            }

            State.Forecast -> {
            }
//todo теперь это не нужно. вроде
            else -> throw IllegalArgumentException("Unknown state: $state")
        }    }

    override fun showData(weatherEntity: WeatherEntity, aqiEntity: AqiEntity) {
        setupFirst(weatherEntity, aqiEntity)
    }

    override fun showForecastData(data: ForecastEntity) {
        setupForecast(data)
    }

}
