package com.iaruchkin.deepbreath.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*

import android.widget.ImageView
import android.widget.TextView
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
import com.iaruchkin.deepbreath.ui.adapter.ExpandableHeaderItem
import com.iaruchkin.deepbreath.ui.adapter.FancyItem
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.groupie_fragment.*
import java.util.*

class GroupieFragment : MvpAppCompatFragment(), AqiView{

    //    private val LAYOUT = R.layout.layout_detail
    private val LAYOUT = R.layout.groupie_fragment


    internal var context1 = App.INSTANCE.getApplicationContext()

    internal val FORECAST_ID = "extra:forecast"
    internal val WEATHER_ID = "extra:weather"
    internal val AQI_ID = "extra:aqi"
    internal val VIEW_TYPE = "extra:viewType"

    private val compositeDisposable = CompositeDisposable()
    val TAG = GroupieFragment::class.java.getSimpleName()
    private var listener: MessageFragmentListener? = null

    @InjectPresenter
    internal var aqiPresenter: AqiPresenter? = null

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

    override fun  onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val boringFancyItems = generateFancyItems(6)
        val excitingFancyItems = generateFancyItems(12)

        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            spanCount = 3
        }

        recycler_view.apply {
            layoutManager = GridLayoutManager(context1, groupAdapter.spanCount).apply {
                spanSizeLookup = groupAdapter.spanSizeLookup
            }
            adapter = groupAdapter
        }

        ExpandableGroup(ExpandableHeaderItem("Boring Group"), true).apply {
            add(Section(boringFancyItems))
            groupAdapter.add(this)
        }

        ExpandableGroup(ExpandableHeaderItem("Exciting Group"), false).apply {
            excitingSection.addAll(excitingFancyItems)
            add(excitingSection)
            groupAdapter.add(this)
        }

        fab.setOnClickListener {
            excitingFancyItems.shuffle()
            excitingSection.update(excitingFancyItems)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(LAYOUT, container, false)

//        findViews(view)
//        setupToolbar()

//        setSupportActionBar(toolbar)

//        val boringFancyItems = generateFancyItems(6)
//        val excitingFancyItems = generateFancyItems(12)
//
//        val groupAdapter = GroupAdapter<ViewHolder>().apply {
//            spanCount = 3
//        }
//
//        recycler_view.apply {
//            layoutManager = GridLayoutManager(context1, groupAdapter.spanCount).apply {
//                spanSizeLookup = groupAdapter.spanSizeLookup
//            }
//            adapter = groupAdapter
//        }

//        ExpandableGroup(ExpandableHeaderItem("Boring Group"), true).apply {
//            add(Section(boringFancyItems))
//            groupAdapter.add(this)
//        }
//
//        ExpandableGroup(ExpandableHeaderItem("Exciting Group"), false).apply {
//            excitingSection.addAll(excitingFancyItems)
//            add(excitingSection)
//            groupAdapter.add(this)
//        }

//        fab.setOnClickListener {
//            excitingFancyItems.shuffle()
//            excitingSection.update(excitingFancyItems)
//        }

        return view
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)
//
//        val boringFancyItems = generateFancyItems(6)
//        val excitingFancyItems = generateFancyItems(12)
//
//        val groupAdapter = GroupAdapter<ViewHolder>().apply {
//            spanCount = 3
//        }
//
//        recycler_view.apply {
//            layoutManager = GridLayoutManager(this@GroupieFragment, groupAdapter.spanCount).apply {
//                spanSizeLookup = groupAdapter.spanSizeLookup
//            }
//            adapter = groupAdapter
//        }
//
//        ExpandableGroup(ExpandableHeaderItem("Boring Group"), true).apply {
//            add(Section(boringFancyItems))
//            groupAdapter.add(this)
//        }
//
//        ExpandableGroup(ExpandableHeaderItem("Exciting Group"), false).apply {
//            excitingSection.addAll(excitingFancyItems)
//            add(excitingSection)
//            groupAdapter.add(this)
//        }
//
//        fab.setOnClickListener {
//            excitingFancyItems.shuffle()
//            excitingSection.update(excitingFancyItems)
//        }
//    }

    private fun generateFancyItems(count: Int): MutableList<FancyItem>{
        val rnd = Random()
        return MutableList(count){
            val color = Color.argb(255, rnd.nextInt(256),
                    rnd.nextInt(256), rnd.nextInt(256))
            FancyItem(color, rnd.nextInt(100))
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }




    override fun showWeatherData(data: WeatherEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showForecastData(data: ForecastEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAqiData(data: AqiEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showState(state: State) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
