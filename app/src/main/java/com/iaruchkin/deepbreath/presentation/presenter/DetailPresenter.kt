package com.iaruchkin.deepbreath.presentation.presenter

import android.util.Log
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.common.BasePresenter
import com.iaruchkin.deepbreath.presentation.view.DetailView
import com.iaruchkin.deepbreath.room.converters.ConverterAqi
import com.iaruchkin.deepbreath.room.converters.ConverterCondition
import com.iaruchkin.deepbreath.room.converters.ConverterOpenForecast
import com.iaruchkin.deepbreath.room.converters.ConverterOpenWeather
import com.iaruchkin.deepbreath.room.entities.AqiEntity
import com.iaruchkin.deepbreath.room.entities.ConditionEntity
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState

@InjectViewState
class DetailPresenter(private val ID_FORECAST: String, private val ID_WEATHER: String, private val ID_AQI: String, private val ID_CONDITION: String, private val VIEW_TYPE: Int) : BasePresenter<DetailView?>() {
    private val context = App.INSTANCE.applicationContext
    private var forecastEntity: ForecastEntity? = null
    private var weatherEntity: WeatherEntity? = null
    private var conditionEntity: ConditionEntity? = null
    private var aqiEntity: AqiEntity? = null
    private val PRESENTER_WEATHER_TAG = "[detail - presenter]"

    override fun onFirstViewAttach() {
        setView()
    }

    private fun setView() {
        if (VIEW_TYPE == 0) {
            loadCurrent()
        } else {
            loadForecast()
        }
    }

    private fun loadCurrent() {
        loadWeatherFromDb(ID_WEATHER)
        loadAqiFromDb(ID_AQI)
        loadConditionFromDb(ID_CONDITION)
    }

    private fun loadForecast() {
        loadForecastFromDb(ID_FORECAST)
        loadConditionFromDb(ID_CONDITION)
    }

    /**work with database
     *
     * @param id
     */
    private fun loadWeatherFromDb(id: String) {
        val loadFromDb = Single.fromCallable {
            ConverterOpenWeather
                    .getDataById(context, id)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: WeatherEntity? -> updateWeatherData(data) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
    }

    private fun loadAqiFromDb(id: String) {
        val loadFromDb = Single.fromCallable {
            ConverterAqi
                    .getDataById(context, id)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: AqiEntity? -> updateAqiData(data) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load AqiData from db")
    }

    private fun loadForecastFromDb(id: String) {
        val loadFromDb = Single.fromCallable {
            ConverterOpenForecast
                    .getDataById(context, id)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: ForecastEntity? -> updateForecastData(data) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load Forecast from db")
    }

    private fun loadConditionFromDb(id: String) {
        val loadFromDb = Single.fromCallable {
            ConverterCondition
                    .getDataById(context, id)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: ConditionEntity? -> updateConditionData(data) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
    }

    /**setting data to app
     *
     * @param data
     */
    private fun updateWeatherData(data: WeatherEntity?) {
        weatherEntity = data
        updateData()
        Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data!!.id + " / " + data.location)
    }

    private fun updateForecastData(data: ForecastEntity?) {
        forecastEntity = data
        updateData()
        Log.i(PRESENTER_WEATHER_TAG, "loaded ForecastData from DB: " + data!!.id + " / " + data.locationName)
    }

    private fun updateAqiData(data: AqiEntity?) {
        aqiEntity = data
        updateData()
        Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data!!.id + " / " + data.aqi)
    }

    private fun updateConditionData(data: ConditionEntity?) {
        Log.w(PRESENTER_WEATHER_TAG, "init condition")
        conditionEntity = data
        updateData()
        Log.i(PRESENTER_WEATHER_TAG, "loaded condition from DB: " + data!!.id + " / " + data.dayText)
    }

    private fun updateData() {
        if (weatherEntity != null && aqiEntity != null && conditionEntity != null) {
            viewState!!.showTodayData(weatherEntity!!, aqiEntity!!, conditionEntity!!)
        } else if (forecastEntity != null && conditionEntity != null) {
            viewState!!.showForecastData(forecastEntity!!, conditionEntity!!)
        }
    }

    /**handling error
     *
     * @param th
     */
    private fun handleError(th: Throwable) {
        Log.e(PRESENTER_WEATHER_TAG, th.message, th)
    }

}