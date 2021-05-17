package com.iaruchkin.deepbreath.presentation.presenter

import android.location.Location
import android.util.Log
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.common.AppPreferences
import com.iaruchkin.deepbreath.common.BasePresenter
import com.iaruchkin.deepbreath.common.State
import com.iaruchkin.deepbreath.network.dtos.AqiAvResponse
import com.iaruchkin.deepbreath.network.dtos.AqiResponse
import com.iaruchkin.deepbreath.network.dtos.OpenWeatherResponse
import com.iaruchkin.deepbreath.network.dtos.aqiAvDTO.AqiAvData
import com.iaruchkin.deepbreath.network.dtos.aqicnDTO.AqiData
import com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO.OfflineCondition.WeatherCondition
import com.iaruchkin.deepbreath.network.parsers.AqiApi
import com.iaruchkin.deepbreath.network.parsers.AqiAvApi
import com.iaruchkin.deepbreath.network.parsers.ConditionParser
import com.iaruchkin.deepbreath.network.parsers.OpenWeatherApi
import com.iaruchkin.deepbreath.presentation.view.ForecastView
import com.iaruchkin.deepbreath.room.AppDatabase
import com.iaruchkin.deepbreath.room.converters.ConverterAqi
import com.iaruchkin.deepbreath.room.converters.ConverterCondition
import com.iaruchkin.deepbreath.room.converters.ConverterOpenForecast
import com.iaruchkin.deepbreath.room.converters.ConverterOpenForecast.saveAllDataToDb
import com.iaruchkin.deepbreath.room.converters.ConverterOpenWeather
import com.iaruchkin.deepbreath.room.converters.ConverterOpenWeather.saveAllDataToDb
import com.iaruchkin.deepbreath.room.entities.*
import com.iaruchkin.deepbreath.utils.LangUtils
import com.iaruchkin.deepbreath.utils.LocationUtils
import com.iaruchkin.deepbreath.utils.PreferencesHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import java.util.*

@InjectViewState
class ForecastPresenter(isGps: Boolean, isSearch: Boolean, location: Location?) : BasePresenter<ForecastView?>() {
    private val PRESENTER_WEATHER_TAG = "[list - presenter]"

    private val context = App.INSTANCE.applicationContext

    private var forecastEntity: List<ForecastEntity>? = null
    private var weatherEntity: List<WeatherEntity>? = null
    private var conditionEntity: List<ConditionEntity>? = null
    //    private List<FavoritesEntity> favoritesEntity;

    private var aqiEntity: List<AqiEntity>? = null
    private var aqiCurrentLocation = "here"
    private var mIsGps = false
    private var mSearch = false
    private var mSearchLocation: Location? = null

    override fun onFirstViewAttach() {
        if (mSearch) {
            loadData(false, mSearchLocation)
        } else {
            loadData(false, PreferencesHelper.getLocation(context))
        }
    }

    fun update() {
        if (mSearch) {
            loadData(false, mSearchLocation)
        } else {
            loadData(false, PreferencesHelper.getLocation(context))
        }
    }

    fun addToFavorites(aqi: Int, city: String) {
        addDataToFavorites(FavoritesEntity(
                "geo" + mSearchLocation!!.latitude + mSearchLocation!!.longitude,
                city,
                mSearchLocation!!.latitude,
                mSearchLocation!!.longitude,
                aqi
        ))
    }

    private fun loadData(forceLoad: Boolean, location: Location?) { //todo почему передаем?
        if (mIsGps) aqiCurrentLocation = PreferencesHelper.getAqiParameter(context)
        val searchLocation = String.format(Locale.ENGLISH, "geo:%s;%s", location!!.latitude, location.longitude)
        if (!forceLoad) {
            if (!mSearch) {
                loadAqiFromDb(aqiCurrentLocation)
            } else {
                loadAqiFromDb(searchLocation)
            }
            loadForecastFromDb(location) //todo выяснить почему префы не работают
            loadWeatherFromDb(location)
            loadConditionFromDb()
        } else {
            loadAqiFromNet(aqiCurrentLocation)
            loadForecastFromNet(location)
            loadCondition()
        }
    }

    /**work with database
     *
     * @param geo
     */
    private fun loadWeatherFromDb(geo: Location) {
        val loadFromDb = Single.fromCallable {
            ConverterOpenWeather
                    .getDataByParameter(context, geo.toString())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<WeatherEntity> -> updateWeatherData(data, geo) }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadForecastFromDb(geo: Location?) {
        val loadFromDb = Single.fromCallable {
            ConverterOpenForecast
                    .getDataByParameter(context, geo.toString())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<ForecastEntity> -> updateForecastData(data, geo!!) }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadAqiFromDb(geo: String) {
        val loadFromDb = Single.fromCallable {
            ConverterAqi
                    .getDataByParameter(context, geo)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<AqiEntity> -> updateAqiData(data, geo) }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load AqiData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadConditionFromDb() {
        val loadFromDb = Single.fromCallable {
            ConverterCondition
                    .getDataByLang(context, LangUtils.getLangCode())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<ConditionEntity>? -> updateConditionData(data) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState!!.showState(State.HasData)
    }

    /**check db responce
     *
     * @param data
     * @param geo
     */
    private fun updateWeatherData(data: List<WeatherEntity>, geo: Location) {
        if (data.isEmpty()) {
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for weatherCurrentLocation : $geo")
            loadForecastFromNet(geo)
        } else {
            weatherEntity = data
            updateWeather()
            Log.i(PRESENTER_WEATHER_TAG, "loaded WeatherData from DB: " + data[0].id + " / " + data[0].location)
            Log.i(PRESENTER_WEATHER_TAG, "update WeatherData executed on thread: " + Thread.currentThread().name)
        }
    }

    private fun updateForecastData(data: List<ForecastEntity>, geo: Location) {
        if (data.isEmpty()) {
            Log.w(PRESENTER_WEATHER_TAG, "there is no WeatherData for weatherCurrentLocation : $geo")
            loadForecastFromNet(geo)
        } else {
            forecastEntity = data
            updateWeather()
            Log.i(PRESENTER_WEATHER_TAG, "loaded ForecastData from DB: " + data[0].id + " / " + data[0].locationName)
            Log.i(PRESENTER_WEATHER_TAG, "update ForecastData executed on thread: " + Thread.currentThread().name)
        }
    }

    private fun updateAqiData(data: List<AqiEntity>, parameter: String) {
        if (data.isEmpty()) {
            Log.w(PRESENTER_WEATHER_TAG, "there is no AqiData for location : $parameter")
            loadAqiFromNet(parameter)
        } else {
            aqiEntity = data
            updateAqi()
            Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data[0].id + " / " + data[0].aqi)
            Log.i(PRESENTER_WEATHER_TAG, "update AqiData executed on thread: " + Thread.currentThread().name)
            if (!mIsGps) {
                val lat = data[0].locationLat
                val lon = data[0].locationLon
                AppPreferences.setLocationDetails(context, lon, lat)
            }
        }
    }

    private fun updateConditionData(data: List<ConditionEntity>?) {
        if (data!!.size == 0) {
            Log.w(PRESENTER_WEATHER_TAG, "init condition")
            loadCondition()
        } else {
            conditionEntity = data
            updateWeather()
            Log.i(PRESENTER_WEATHER_TAG, "loaded condition from DB: " + data[0].id + " / " + data[0].dayText)
            Log.i(PRESENTER_WEATHER_TAG, "update condition executed on thread: " + Thread.currentThread().name)
        }
    }

    /**work with internet
     *
     * @param parameter
     */
    private fun loadForecastFromNet(parameter: Location) {
        Log.i(PRESENTER_WEATHER_TAG, "Load Forecast from net presenter")
        viewState!!.showState(State.Loading)
        val disposable = OpenWeatherApi.getInstance()
                .openWeatherEndpoint()[parameter.latitude.toString(), parameter.longitude.toString()]
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: OpenWeatherResponse ->
                    updateForecastDB(response, parameter.toString())
                    updateWeatherDB(response, parameter.toString())
                }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(disposable)
    }

    private fun loadAqiFromNet(parameter: String) {
        Log.i(PRESENTER_WEATHER_TAG, "Load AQI from net presenter")
        viewState!!.showState(State.LoadingAqi)
        val disposable = AqiApi.getInstance()
                .aqiEndpoint()[parameter]
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: AqiResponse ->
                    val lat = response.aqiData.city.geo[0]
                    val lon = response.aqiData.city.geo[1]
                    val isValid = LocationUtils.locationIsValid(lat, lon, context)
                    if (!isValid) loadAqiAvFromNet(parameter) else updateAqiDB(response, parameter)
                    if (!mIsGps) {
                        AppPreferences.setLocationDetails(context, lat, lon)
                    }
                }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(disposable)
    }

    private fun loadAqiAvFromNet(parameter: String) {
        Log.w(PRESENTER_WEATHER_TAG, "Load AQIav from net presenter")
        viewState!!.showState(State.LoadingAqi)
        val disposable = AqiAvApi.getInstance()
                .aqiAvEndpoint()[PreferencesHelper.getLocation(context).latitude, PreferencesHelper.getLocation(context).longitude]
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: AqiAvResponse -> updateAqiAvDB(response, parameter) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(disposable)
    }

    private fun loadCondition() {
        val disposable = Single.fromCallable { ConditionParser.getInstance().conditions() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: List<WeatherCondition> -> updateCondition(response) }) { th: Throwable -> handleError(th) }
        disposeOnDestroy(disposable)
    }

    /**update database
     *
     * @param response
     * @param parameter
     */
    private fun updateWeatherDB(response: OpenWeatherResponse?, parameter: String) {
        if (response == null) {
            viewState!!.showState(State.HasNoData)
        } else {
            val saveWeatherToDb = Single.fromCallable { response }
                    .subscribeOn(Schedulers.io())
                    .map { weatherDTO: OpenWeatherResponse? ->
                        saveAllDataToDb(context,
                                ConverterOpenWeather.dtoToDao(weatherDTO!!, parameter), parameter)
                        ConverterOpenWeather.getDataByParameter(context, parameter)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { weatherEntities: List<WeatherEntity> ->
                                weatherEntity = weatherEntities
                                updateWeather()
                                Log.i(PRESENTER_WEATHER_TAG, "loaded weather from NET to DB, size: " + weatherEntities.size)
                            }) { th: Throwable -> handleDbError(th) }
            disposeOnDestroy(saveWeatherToDb)
            viewState!!.showState(State.HasData)
        }
    }

    private fun updateForecastDB(response: OpenWeatherResponse, parameter: String) {
        if (response.list.isEmpty()) {
            viewState!!.showState(State.HasNoData)
        } else {
            val saveForecastToDb = Single.fromCallable { response }
                    .subscribeOn(Schedulers.io())
                    .map { weatherDTO: OpenWeatherResponse? ->
                        saveAllDataToDb(context,
                                ConverterOpenForecast.dtoToDao(weatherDTO!!, parameter), parameter)
                        ConverterOpenForecast.getDataByParameter(context, parameter)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { forecastEntities: List<ForecastEntity> ->
                                forecastEntity = forecastEntities
                                updateWeather()
                                Log.i(PRESENTER_WEATHER_TAG, "loaded forecast from NET to DB, size: " + forecastEntities.size)
                            }) { th: Throwable -> handleDbError(th) }
            disposeOnDestroy(saveForecastToDb)
            viewState!!.showState(State.HasData)
        }
    }

    private fun updateAqiDB(response: AqiResponse, parameter: String) {
        if (response.aqiData == null) {
            viewState!!.showState(State.HasNoData)
            Log.w(PRESENTER_WEATHER_TAG, "no data!")
        } else {
            val saveDataToDb = Single.fromCallable { response.aqiData }
                    .subscribeOn(Schedulers.io())
                    .map { aqiDTO: AqiData? ->
                        ConverterAqi.saveAllDataToDb(context,
                                ConverterAqi.dtoToDao(aqiDTO, parameter), parameter)
                        ConverterAqi.getDataByParameter(context, parameter)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { aqiEntities: List<AqiEntity> ->
                                aqiEntity = aqiEntities
                                updateAqi()
                                Log.i(PRESENTER_WEATHER_TAG, "loaded aqi from NET to DB, size: " + aqiEntities.size)
                            }) { th: Throwable -> handleDbError(th) }
            disposeOnDestroy(saveDataToDb)
            viewState!!.showState(State.HasData)
        }
    }

    private fun updateAqiAvDB(response: AqiAvResponse, parameter: String) {
        if (response.aqiAvData == null) {
            viewState!!.showState(State.HasNoData)
        } else {
            val saveDataToDb = Single.fromCallable { response.aqiAvData }
                    .subscribeOn(Schedulers.io())
                    .map { aqiDTO: AqiAvData? ->
                        ConverterAqi.saveAllDataToDb(context,
                                ConverterAqi.dtoToDao(aqiDTO, parameter), parameter)
                        ConverterAqi.getDataByParameter(context, parameter)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { aqiEntities: List<AqiEntity> ->
                                aqiEntity = aqiEntities
                                updateAqi()
                                Log.i(PRESENTER_WEATHER_TAG, "loaded aqi from NET to DB, size: " + aqiEntities.size)
                            }) { th: Throwable -> handleDbError(th) }
            disposeOnDestroy(saveDataToDb)
            viewState!!.showState(State.HasData)
        }
    }

    private fun updateCondition(response: List<WeatherCondition>) {
        val saveDataToDb = Single.fromCallable { response }
                .subscribeOn(Schedulers.io())
                .map { conditions: List<WeatherCondition>? ->
                    ConverterCondition.saveAllDataToDb(context,
                            ConverterCondition.dtoToDao(conditions, LangUtils.getLangCode()))
                    ConverterCondition.loadDataFromDb(context)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { conditionEntities: List<ConditionEntity> ->
                    conditionEntity = conditionEntities
                    updateWeather()
                    Log.i(PRESENTER_WEATHER_TAG, "loaded condition from NET to DB, size: " + conditionEntities.size)
                }
        disposeOnDestroy(saveDataToDb)
        viewState!!.showState(State.HasData)
    }

    private fun addDataToFavorites(entity: FavoritesEntity) {
        val saveDataToDb = Single.fromCallable { entity }
                .subscribeOn(Schedulers.io()) //                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { aqiEntities: FavoritesEntity ->
                            val db = AppDatabase.getAppDatabase(context)
                            db.favoritesDao().insert(entity)
                            Log.i(PRESENTER_WEATHER_TAG, "saved to favorites: " + aqiEntities.locationName)
                        }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(saveDataToDb)
        viewState!!.showState(State.HasData)
    }

    /**setting data objects
     *
     */
    private fun updateAqi() {
        if (aqiEntity != null) {
            viewState!!.showAqi(aqiEntity!!)
            viewState!!.showState(State.HasData)
        }
    }

    private fun updateWeather() {
        if (forecastEntity != null && weatherEntity != null && conditionEntity != null) {
            viewState!!.showWeather(forecastEntity!!, weatherEntity!!, conditionEntity!!)
            viewState!!.showState(State.HasData)
        }
    }

    /**handling errors
     *
     * @param th
     */
    private fun handleError(th: Throwable) {
        viewState!!.showState(State.NetworkError)
        loadWeatherFromDb()
        loadForecastFromDb()
        loadAQIFromDb()
        Log.e(PRESENTER_WEATHER_TAG, th.message, th)
    }

    private fun loadWeatherFromDb() {
        val loadFromDb = Single.fromCallable {
            ConverterOpenWeather
                    .getLastData(context)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<WeatherEntity>? ->
                            weatherEntity = data
                            updateWeather()
                        }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.e(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadForecastFromDb() {
        val loadFromDb = Single.fromCallable {
            ConverterOpenForecast
                    .getLastData(context)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<ForecastEntity>? ->
                            forecastEntity = data
                            updateWeather()
                        }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.e(PRESENTER_WEATHER_TAG, "Load ForecastData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadAQIFromDb() {
        val loadFromDb = Single.fromCallable {
            ConverterAqi
                    .getLastData(context)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<AqiEntity>? ->
                            aqiEntity = data
                            updateAqi()
                        }) { th: Throwable -> handleDbError(th) }
        disposeOnDestroy(loadFromDb)
        Log.e(PRESENTER_WEATHER_TAG, "Load AQIData from db")
        viewState!!.showState(State.HasData)
    }

    private fun handleDbError(th: Throwable) {
        viewState!!.showState(State.DbError)
        Log.e(PRESENTER_WEATHER_TAG, th.message, th)
    }

    init {
        mIsGps = isGps
        mSearch = isSearch
        mSearchLocation = location
    }
}