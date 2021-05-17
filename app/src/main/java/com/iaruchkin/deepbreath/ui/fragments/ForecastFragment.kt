package com.iaruchkin.deepbreath.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
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
import com.google.android.material.snackbar.Snackbar
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.presentation.presenter.ForecastPresenter
import com.iaruchkin.deepbreath.presentation.view.ForecastView
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.ConditionEntity
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import com.iaruchkin.deepbreath.ui.*
import com.iaruchkin.deepbreath.ui.adapter.ForecastAdapter
import com.iaruchkin.deepbreath.ui.adapter.ForecastAdapter.ForecastAdapterOnClickHandler
import com.iaruchkin.deepbreath.utils.AqiUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import io.reactivex.disposables.CompositeDisposable
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*

class ForecastFragment : MvpAppCompatFragment(), ForecastAdapterOnClickHandler, ForecastView, OnRefreshListener {

//    private val presenter by moxyPresenter { todo try delegates
//        ForecastPresenter(false, false, null)
//    }

    @JvmField
    @InjectPresenter
    var forecastPresenter: ForecastPresenter? = null
    private var mListener: MessageFragmentListener? = null
    private var mAdapter: ForecastAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mError: View? = null
    private var mIsSearch: Boolean = false
    private var errorAction: Button? = null
    private var mRefresh: SwipeRefreshLayout? = null
    private var nWeatherItem: WeatherEntity? = null
    private var mAqiItem: AqiEntity? = null
    private var toolbar: Toolbar? = null
    private val compositeDisposable = CompositeDisposable()

    @ProvidePresenter
    fun providePresenter(): ForecastPresenter {
        val isGPS = requireArguments().getBoolean("GEO", false)
        val isSearch = requireArguments().getBoolean("SEARCH", false)
        val location = requireArguments().getDoubleArray("LOCATION")

        mIsSearch = isSearch

        return ForecastPresenter(
                isGPS,
                isSearch,
                Location("Search Location").apply {
                    latitude = location?.get(0) ?: 0.0
                    longitude = location?.get(1) ?: 0.0
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.i(WEATHER_LIST_TAG, "OnCreateView executed on thread:" + Thread.currentThread().name)
        val view = inflater.inflate(LAYOUT, container, false)
        setupUi(view)
        setupUx()
        return view
    }

    override fun onStart() {
        Log.i(WEATHER_LIST_TAG, "onStart")
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
            mListener = context
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
        if (mIsSearch) {
            menu.removeItem(R.id.action_find)
        } else {
            menu.removeItem(R.id.action_favorite)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                if (mListener != null) {
                    mListener!!.onActionClicked(SETTINGS_TAG)
                }
                true
            }
            R.id.action_about -> {
                if (mListener != null) {
                    mListener!!.onActionClicked(ABOUT_TAG)
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
            R.id.action_favorite -> {
                addToFavorites()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun findCity() {
        if (mListener != null) {
            mListener!!.onActionClicked(FIND_TAG)
        }
    }

    private fun addToFavorites() {
        forecastPresenter?.addToFavorites()
    }

    @SuppressLint("StringFormatMatches")
    private fun share() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        var message = getString(R.string.github_link)
        if (mAqiItem != null && nWeatherItem != null) {
            message = String.format(Locale.getDefault(), getString(R.string.share_message), StringUtils.transliterateLatToRus(nWeatherItem!!.location, nWeatherItem!!.country), resources.getString(AqiUtils.getPollutionLevel(mAqiItem!!.aqi)), getString(R.string.google_play_link))
        }
        i.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(i, getString(R.string.share)))
    }

    override fun onClickList(forecastItem: ForecastEntity, weatherEntity: WeatherEntity, aqiEntity: AqiEntity, conditionEntity: ConditionEntity, viewType: Int) {
        mListener!!.onListClicked(forecastItem.id, weatherEntity.id, aqiEntity.id, conditionEntity.id, viewType)
    }

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (context as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val actionBar = (context as AppCompatActivity?)!!.supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.title = if (!mIsSearch)
            resources.getString(R.string.app_name)
        else "Поиск станции" //todo string res
    }

    private fun setHomeButton(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (context as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val supportActionBar = (context as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(mIsSearch)
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
        forecastPresenter?.update()
        mListener!!.onActionClicked(GET_LOCATION)
    }

    private fun setupUx() {
        errorAction!!.setOnClickListener { v: View? -> mListener!!.onActionClicked(WEATHER_LIST_TAG) }
    }

    override fun showWeather(forecastEntity: List<ForecastEntity>,
                             weatherEntity: List<WeatherEntity>,
                             conditionEntity: List<ConditionEntity>) {
        if (forecastEntity.isNotEmpty() && weatherEntity.isNotEmpty() && conditionEntity.isNotEmpty()) {
            mAdapter!!.setWeather(forecastEntity, weatherEntity[0], conditionEntity)
            nWeatherItem = weatherEntity[0]
        }
    }

    override fun showAqi(aqiEntity: List<AqiEntity>) {
        if (aqiEntity.isNotEmpty()) {
            mAdapter!!.setAqi(aqiEntity[0], mIsSearch)
            mAqiItem = aqiEntity[0]
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
        val snackbar = Snackbar.make(requireView(), getString(R.string.error_snack_msg), Snackbar.LENGTH_INDEFINITE)
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
        fun newInstance(isGPS: Boolean, isSearch: Boolean = false, location: List<Double>? = null): ForecastFragment {
            val fragmentForecast = ForecastFragment()
            val bundle = Bundle()
            bundle.putBoolean("GEO", isGPS)
            bundle.putBoolean("SEARCH", isSearch)
            bundle.putDoubleArray("LOCATION", location?.toDoubleArray())
            fragmentForecast.arguments = bundle
            return fragmentForecast
        }
    }
}