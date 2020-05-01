package com.iaruchkin.deepbreath.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.MvpAppCompatFragment
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.presentation.presenter.ForecastPresenter
import com.iaruchkin.deepbreath.presentation.view.ForecastView
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.ConditionEntity
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import com.iaruchkin.deepbreath.ui.MainActivity
import com.iaruchkin.deepbreath.ui.adapter.ForecastAdapter
import com.iaruchkin.deepbreath.ui.adapter.ForecastAdapter.ForecastAdapterOnClickHandler
import com.iaruchkin.deepbreath.utils.AqiUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class ForecastFragment : MvpAppCompatFragment(), ForecastAdapterOnClickHandler, ForecastView, OnRefreshListener {
    private var listener: MessageFragmentListener? = null
    private var weatherItem: WeatherEntity? = null
    private var aqiItem: AqiEntity? = null

    @JvmField
    @InjectPresenter
    var forecastPresenter: ForecastPresenter? = null
    private var mAdapter: ForecastAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mError: View? = null
    private var errorAction: Button? = null
    private var mRefresh: SwipeRefreshLayout? = null
    private var toolbar: Toolbar? = null
    private val compositeDisposable = CompositeDisposable()

    @ProvidePresenter
    fun providePresenter(): ForecastPresenter {
        val isGPS = if (arguments != null) arguments!!.getBoolean("GEO", false) else null
        return ForecastPresenter(isGPS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.i(MainActivity.WEATHER_LIST_TAG, "OnCreateView executed on thread:" + Thread.currentThread().name)
        val view = inflater.inflate(LAYOUT, container, false)
        setupUi(view)
        setupUx()
        return view
    }

    override fun onStart() {
        Log.i(MainActivity.WEATHER_LIST_TAG, "onStart")
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter = null
        mRecyclerView = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageFragmentListener) {
            listener = context
        }
    }

    private fun setupUi(view: View) {
        findViews(view)
        setupToolbar()
        setupOrientation()
        setupRecyclerViewAdapter()
        setHomeButton(view)
        mRefresh!!.setOnRefreshListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forecast, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                if (listener != null) {
                    listener!!.onActionClicked(MainActivity.SETTINGS_TAG)
                }
                true
            }
            R.id.action_about -> {
                if (listener != null) {
                    listener!!.onActionClicked(MainActivity.ABOUT_TAG)
                }
                true
            }
            R.id.action_share -> {
                share()
                true
            }
            R.id.action_find -> {
                findCity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findCity() {
        if (listener != null) {
            listener!!.onActionClicked(MainActivity.FIND_TAG)
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun share() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var message = getString(R.string.github_link)
        if (aqiItem != null && weatherItem != null) {
            message = String.format(Locale.getDefault(), getString(R.string.share_message)
                    , StringUtils.transliterateLatToRus(weatherItem!!.location, weatherItem!!.country)
                    , resources.getString(AqiUtils.getPollutionLevel(aqiItem!!.aqi))
                    , getString(R.string.google_play_link))
        }
        i.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(i, getString(R.string.share)))
    }

    override fun onClickList(forecastItem: ForecastEntity, weatherEntity: WeatherEntity, aqiEntity: AqiEntity, conditionEntity: ConditionEntity, viewType: Int) {
        listener!!.onListClicked(forecastItem.id, weatherEntity.id, aqiEntity.id, conditionEntity.id, viewType)
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (context as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as AppCompatActivity?)!!.supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.title = resources.getString(R.string.app_name)
    }

    private fun setHomeButton(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (context as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val supportActionBar = (context as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupRecyclerViewAdapter() {
        mAdapter = ForecastAdapter(this)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun setupOrientation() {
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)

        //todo fix in future versions
        //ability to rotate the screen
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        } else {
//            final int columnsCount = getResources().getInteger(R.integer.landscape_news_columns_count);
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnsCount));
//        }
    }

    override fun onRefresh() {
        //TODO надо просто view обновить а мы тут пересоздаем фрагмент! зачем?
        forecastPresenter?.update()
//        listener!!.onActionClicked(MainActivity.WEATHER_LIST_TAG, true)
        listener!!.onActionClicked(MainActivity.GET_LOCATION)
    }

    private fun setupUx() {
        errorAction!!.setOnClickListener { v: View? -> listener!!.onActionClicked(MainActivity.WEATHER_LIST_TAG) }
    }

    override fun showWeather(forecastEntity: List<ForecastEntity>,
                             weatherEntity: List<WeatherEntity>,
                             conditionEntity: List<ConditionEntity>) {
        if (forecastEntity.isNotEmpty() && weatherEntity.isNotEmpty() && conditionEntity.isNotEmpty()) {
            mAdapter!!.setWeather(forecastEntity, weatherEntity[0], conditionEntity)
            weatherItem = weatherEntity[0]
        }
    }

    override fun showAqi(aqiEntity: List<AqiEntity>) {
        if (aqiEntity.isNotEmpty()) {
            mAdapter!!.setAqi(aqiEntity[0])
            aqiItem = aqiEntity[0]
        }
    }

    override fun showState(state: State) {
        when (state) {
            State.HasData -> {
                mRefresh!!.visibility = View.VISIBLE
                mRecyclerView!!.visibility = View.VISIBLE
                mError!!.visibility = View.GONE
                showRefresher(false)
            }
            State.HasNoData -> {
                mRefresh!!.visibility = View.GONE
                mError!!.visibility = View.VISIBLE
                showRefresher(false)
            }
            State.NetworkError -> {
                mRefresh!!.visibility = View.GONE
                mError!!.visibility = View.GONE
                showRefresher(false)
                showErrorSnack()
            }
            State.DbError -> {
                mRefresh!!.visibility = View.GONE
                mError!!.visibility = View.VISIBLE
                showRefresher(false)
            }
            State.Loading -> {
                mRefresh!!.visibility = View.VISIBLE
                mRecyclerView!!.visibility = View.VISIBLE
                mError!!.visibility = View.GONE
                showRefresher(true)
            }
            State.LoadingAqi -> {
                mRefresh!!.visibility = View.VISIBLE
                mRecyclerView!!.visibility = View.VISIBLE
                mError!!.visibility = View.GONE
                showRefresher(false)
            }
            else -> throw IllegalArgumentException("Unknown state: $state")
        }
    }

    private fun showRefresher(show: Boolean) {
        mRefresh!!.isRefreshing = show
    }

    private fun showErrorSnack() {
        val snackbar = Snackbar.make(view!!, getString(R.string.error_snack_msg), Snackbar.LENGTH_INDEFINITE)
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(resources.getColor(R.color.error_snack))
        snackbar.show()
    }

    private fun findViews(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        mRecyclerView = view.findViewById(R.id.idRecyclerView)
        mError = view.findViewById(R.id.error_layout)
        errorAction = view.findViewById(R.id.action_button)
        mRefresh = view.findViewById(R.id.refresh)
    }

    companion object {
        private const val LAYOUT = R.layout.fragment_weather_list

        @JvmStatic
        fun newInstance(isGPS: Boolean?): ForecastFragment {
            val fragmentForecast = ForecastFragment()
            val bundle = Bundle()
            bundle.putBoolean("GEO", isGPS!!)
            fragmentForecast.arguments = bundle
            return fragmentForecast
        }
    }
}