package com.iaruchkin.deepbreath.screens.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.iaruchkin.deepbreath.screens.*
import com.iaruchkin.deepbreath.screens.adapter.ForecastAdapter
import com.iaruchkin.deepbreath.screens.adapter.ForecastAdapter.ForecastAdapterOnClickHandler
import com.iaruchkin.deepbreath.utils.AqiUtils
import com.iaruchkin.deepbreath.utils.StringUtils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_weather_list.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.toolbar.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.util.*

class ForecastFragment : MvpAppCompatFragment(), ForecastAdapterOnClickHandler, ForecastView, OnRefreshListener {

    @JvmField
    @InjectPresenter
    var forecastPresenter: ForecastPresenter? = null

    private var mListener: MessageFragmentListener? = null
    private var mAdapter: ForecastAdapter? = null
    private var mIsSearch: Boolean = false
    private var mIsInFavorites: Boolean = false
    private var nWeatherItem: WeatherEntity? = null
    private var mAqiItem: AqiEntity? = null
    private val compositeDisposable = CompositeDisposable()

    @ProvidePresenter
    fun providePresenter(): ForecastPresenter {
        val isGPS = requireArguments().getBoolean("GEO", false)
        val isSearch = requireArguments().getBoolean("SEARCH", false)
        val location = requireArguments().getParcelable<Location>("LOCATION")

        mIsSearch = isSearch

        return ForecastPresenter(
                isGPS,
                isSearch,
                location
        )
    }

    fun update() {
        forecastPresenter?.update()
    }

    override fun onClickList(forecastItem: ForecastEntity, weatherEntity: WeatherEntity, aqiEntity: AqiEntity, conditionEntity: ConditionEntity, viewType: Int) {
        mListener!!.onListClicked(forecastItem.id, weatherEntity.id, aqiEntity.id, conditionEntity.id, viewType)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupUx()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageFragmentListener) {
            mListener = context
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forecast, menu)
        if (mIsSearch) {
            forecastPresenter?.inFavoritesCheck()
            menu.removeItem(R.id.action_find)
            menu.findItem(R.id.action_favorite).setIcon(
                    if (mIsInFavorites) {
                        R.drawable.ic_bookmark_added
                    } else {
                        R.drawable.ic_bookmark_border
                    }
            )
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
                if (mIsInFavorites) {
                    removeFromFavorites()
                } else {
                    addToFavorites()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRefresh() {
        if (!mIsSearch) mListener?.onActionClicked(GET_LOCATION)
        else update()
    }

    override fun showWeather(forecastEntity: List<ForecastEntity>,
                             weatherEntity: List<WeatherEntity>,
                             conditionEntity: List<ConditionEntity>) {
        if (forecastEntity.isNotEmpty() && weatherEntity.isNotEmpty() && conditionEntity.isNotEmpty()) {
            mAdapter?.setWeather(forecastEntity, weatherEntity[0], conditionEntity)
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
                rRefresh?.visibility = View.VISIBLE
                rProgressBar?.visibility = View.GONE
                rForecastRecyclerView?.visibility = View.VISIBLE
                rErrorLayout!!.visibility = View.GONE
                showRefresher(false)
            }
            State.HasNoData -> {
                rRefresh?.visibility = View.GONE
                rProgressBar?.visibility = View.GONE
                rErrorLayout?.visibility = View.VISIBLE
                showRefresher(false)
            }
            State.NetworkError -> {
                rRefresh?.visibility = View.GONE
                rProgressBar?.visibility = View.GONE
                rErrorLayout?.visibility = View.GONE
                showRefresher(false)
                showErrorSnack()
            }
            State.DbError -> {
                rRefresh?.visibility = View.GONE
                rProgressBar?.visibility = View.GONE
                rErrorLayout?.visibility = View.VISIBLE
                showRefresher(false)
            }
            State.Loading -> {
                rRefresh?.visibility = View.VISIBLE
                rProgressBar?.visibility = View.VISIBLE
                rForecastRecyclerView?.visibility = View.VISIBLE
                rErrorLayout?.visibility = View.GONE
                showRefresher(true)
            }
            State.LoadingAqi -> {
                rRefresh?.visibility = View.VISIBLE
                rForecastRecyclerView?.visibility = View.VISIBLE
                (rForecastRecyclerView?.findViewHolderForAdapterPosition(0)
                        as? ForecastAdapter.WeatherViewHolder)?.showState(State.LoadingAqi)
                rErrorLayout?.visibility = View.GONE
                showRefresher(false)
            }
            else -> throw IllegalArgumentException("Unknown state: $state")
        }
    }

    override fun updateIcon(inFavorites: Boolean) {
        mIsInFavorites = inFavorites
        rToolbar?.menu?.findItem(R.id.action_favorite)?.setIcon(
                if (inFavorites) {
                    R.drawable.ic_bookmark_added
                } else {
                    R.drawable.ic_bookmark_border
                }
        )
    }


    private fun showRefresher(show: Boolean) {
        rRefresh?.isRefreshing = show
    }

    private fun showErrorSnack() {
        val snackbar = Snackbar.make(requireView(), getString(R.string.error_snack_msg), Snackbar.LENGTH_INDEFINITE)
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(resources.getColor(R.color.error_snack))
        snackbar.show()
    }

    private fun setupUi(view: View) {
        setupToolbar()
        setupOrientation()
        setupRecyclerViewAdapter()
        setHomeButton(view)
        rRefresh?.setOnRefreshListener(this)
    }

    private fun setupUx() {
        rErrorActionButton?.setOnClickListener { mListener?.onActionClicked(WEATHER_LIST_TAG) }
    }

    private fun findCity() {
        if (mListener != null) {
            mListener!!.onActionClicked(FIND_TAG)
        }
    }

    private fun addToFavorites() {
        forecastPresenter?.updateFavorites()
    }

    private fun removeFromFavorites() {
        forecastPresenter?.updateFavorites(true)
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

    private fun setupToolbar() {
        setHasOptionsMenu(true)
        (context as AppCompatActivity?)!!.setSupportActionBar(rToolbar)
        val actionBar = (context as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.title = if (!mIsSearch)
            resources.getString(R.string.app_name)
        else getString(R.string.station_search)
    }

    private fun setHomeButton(view: View) {
        val toolbar: Toolbar = view.findViewById(R.id.rToolbar)
        (context as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        val supportActionBar = (context as AppCompatActivity?)!!.supportActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(mIsSearch)
    }

    private fun setupRecyclerViewAdapter() {
        if (mAdapter == null) {
            mAdapter = ForecastAdapter(this)
        }
        rForecastRecyclerView?.adapter = mAdapter
    }

    private fun setupOrientation() {
        rForecastRecyclerView?.layoutManager = LinearLayoutManager(context)

        //todo add in future versions, ability to rotate the screen
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        } else {
//            final int columnsCount = getResources().getInteger(R.integer.landscape_news_columns_count);
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), columnsCount));
//        }
    }


    companion object {
        private const val LAYOUT = R.layout.fragment_weather_list

        @JvmStatic
        fun newInstance(isGPS: Boolean, isSearch: Boolean = false, location: Location? = null): ForecastFragment {
            val fragmentForecast = ForecastFragment()
            val bundle = Bundle()
            bundle.putBoolean("GEO", isGPS)
            bundle.putBoolean("SEARCH", isSearch)
            bundle.putParcelable("LOCATION", location)
            fragmentForecast.arguments = bundle
            return fragmentForecast
        }
    }
}