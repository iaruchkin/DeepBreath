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
    private var aqiEntity: List<AqiEntity>? = null
    private var mIsGps = false
    private var mSearch = false
    private var mSearchLocation: Location? = null

    override fun onFirstViewAttach() {
        if (mSearch) {
            loadData(false, mSearchLocation!!)
        } else {
            loadData(false, PreferencesHelper.getLocation(context)) //todo autoupdate by time
        }
    }

    fun update() {
        if (mSearch) {
            loadData(true, mSearchLocation!!)
        } else {
            loadData(true, PreferencesHelper.getLocation(context))
        }
    }

    fun updateFavorites(remove: Boolean = false) {
        val aqiItem = aqiEntity?.get(0)
        val location =  "${(aqiItem?.cityName) ?: forecastEntity?.get(0)?.locationName}"

        if (!remove) {
            addDataToFavorites(FavoritesEntity(
                    mSearchLocation!!.generateLocationRequestId(),
                    location,
                    mSearchLocation!!.latitude,
                    mSearchLocation!!.longitude,
                    aqiItem?.aqi ?: 10
            ))
        } else {
            removeFromFavorites(mSearchLocation!!.generateLocationRequestId())
        }
    }

    fun inFavoritesCheck(){
        checkFavorites(mSearchLocation!!.generateLocationRequestId())
    }

    private fun Location.locationRequest() = if (mIsGps || mSearch) this.generateLocationRequestId() else "here"

    private fun Location.generateLocationRequestId() =
            String.format(Locale.ENGLISH, "geo:%s;%s", this.latitude, this.longitude)

    private fun loadData(forceLoad: Boolean, location: Location) {
        if (!forceLoad) {
            loadAqiFromDb(location)
            loadForecastFromDb(location)
            loadWeatherFromDb(location)
            loadConditionFromDb()
        } else {
            loadAqiFromNet(location)
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
            ConverterOpenWeather.getDataByParameter(context, geo.toString())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<WeatherEntity> -> updateWeatherData(data, geo) })
                { th: Throwable -> handleDbError(th,"loadWeatherFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState?.showState(State.HasData)
    }

    private fun loadForecastFromDb(geo: Location?) {
        val loadFromDb = Single.fromCallable {
            ConverterOpenForecast
                    .getDataByParameter(context, geo.toString())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<ForecastEntity> -> updateForecastData(data, geo!!) })
                { th: Throwable -> handleDbError(th, "loadForecastFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState?.showState(State.HasData)
    }

    private fun loadAqiFromDb(location: Location) {
        val loadFromDb = Single.fromCallable {
            ConverterAqi
                    .getDataByParameter(context, location.locationRequest())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<AqiEntity> -> updateAqiData(data, location) })
                { th: Throwable -> handleDbError(th, "loadAqiFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load AqiData from db")
        viewState?.showState(State.HasData)
    }

    private fun loadConditionFromDb() {
        val loadFromDb = Single.fromCallable {
            ConverterCondition.getDataByLang(context, LangUtils.getLangCode())
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<ConditionEntity> -> updateConditionData(data) })
                { th: Throwable -> handleError(th, "loadConditionFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.i(PRESENTER_WEATHER_TAG, "Load Condition from db")
        viewState?.showState(State.HasData)
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
        }
    }

    private fun updateAqiData(data: List<AqiEntity>, location: Location) {
        if (data.isEmpty()) {
            Log.w(PRESENTER_WEATHER_TAG, "there is no AqiData for location : ${location.locationRequest()}")
            loadAqiFromNet(location)
        } else {
            aqiEntity = data
            updateAqi()
            Log.i(PRESENTER_WEATHER_TAG, "loaded AqiData from DB: " + data[0].id + " / " + data[0].aqi)
            if (!mIsGps && !mSearch) { //TODO выяснить зачем это и оставить пояснение
                AppPreferences.setLocationDetails(context, data[0].getCoordinates())
            }
        }
    }

    private fun updateConditionData(data: List<ConditionEntity>) {
        if (data.isEmpty()) {
            Log.w(PRESENTER_WEATHER_TAG, "init condition")
            loadCondition()
        } else {
            conditionEntity = data
            updateWeather()
            Log.i(PRESENTER_WEATHER_TAG, "loaded condition from DB: " + data[0].id + " / " + data[0].dayText)
        }
    }

    /**network
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
                }) { th: Throwable -> handleError(th, "loadForecastFromNet") }
        disposeOnDestroy(disposable)
    }

    private fun loadAqiFromNet(location: Location) {
        Log.i(PRESENTER_WEATHER_TAG, "Load AQI from net presenter")
        viewState!!.showState(State.LoadingAqi)
        val disposable = AqiApi.getInstance()
                .aqiEndpoint()[ location.locationRequest()]
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: AqiResponse ->
                    val aqiLocation = response.aqiData.city.getCoordinates()
                    val isValid = if (!mSearch) {
                        LocationUtils.locationIsValid(aqiLocation, context)
                    } else LocationUtils.locationIsValid(aqiLocation, location)
                    if (!isValid) {
                        loadAqiAvFromNet(
                                location.locationRequest(),
                                location.latitude,
                                location.longitude
                        )
                    } else {
                        updateAqiDB(response, location.locationRequest())
                    }
                    if (!mIsGps && !mSearch) {
                        AppPreferences.setLocationDetails(context, aqiLocation)
                    }
                }) { th: Throwable -> handleError(th, "loadAqiFromNet") }
        disposeOnDestroy(disposable)
    }

    private fun loadAqiAvFromNet(parameter: String, latitude: Double, longitude: Double ) {
        Log.w(PRESENTER_WEATHER_TAG, "Load AQIav from net presenter")
        viewState!!.showState(State.LoadingAqi)
        val disposable = AqiAvApi.getInstance()
                .aqiAvEndpoint()[latitude, longitude]
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: AqiAvResponse -> updateAqiAvDB(response, parameter) })
                { th: Throwable -> handleError(th, "loadAqiAvFromNet") }
        disposeOnDestroy(disposable)
    }

    private fun loadCondition() {
        val disposable = Single.fromCallable { ConditionParser.getInstance().conditions() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: List<WeatherCondition> -> updateCondition(response) })
                { th: Throwable -> handleError(th, "loadCondition") }
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
                            }) { th: Throwable -> handleDbError(th, "updateWeatherDB") }
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
                            }) { th: Throwable -> handleDbError(th, "updateForecastDB") }
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
                            }) { th: Throwable -> handleDbError(th, "updateAqiDB") }
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
                            }) { th: Throwable -> handleDbError(th, "updateAqiAvDB") }
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
        val saveDataToDb = Single.fromCallable {
            val db = AppDatabase.getAppDatabase(context)
            db.favoritesDao().insert(entity)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState?.updateIcon(true)
                            Log.i(PRESENTER_WEATHER_TAG, "saved to favorites: " + entity.locationName)
                        }) { th: Throwable -> handleDbError(th, "addDataToFavorites") }
        disposeOnDestroy(saveDataToDb)
        viewState?.showState(State.HasData)
    }

    private fun removeFromFavorites(id: String) {
        val removeFromDb = Single.fromCallable {
            val db = AppDatabase.getAppDatabase(context)
            db.favoritesDao().deleteById(id)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState?.updateIcon(false)
                            Log.i(PRESENTER_WEATHER_TAG, "removed from favorites: $id")
                        }) { th: Throwable -> handleDbError(th, "removeFromFavorites") }
        disposeOnDestroy(removeFromDb)
        viewState?.showState(State.HasData)
    }

    private fun checkFavorites(id: String) {
        val getDataFromDb = Single.fromCallable {
            val db = AppDatabase.getAppDatabase(context)
            db.favoritesDao().getFavoriteById(id) != null
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            viewState?.updateIcon(it)
                            Log.i(PRESENTER_WEATHER_TAG, "$id in favorites: $it")
                        }) { th: Throwable -> handleDbError(th, "checkFavorites") }
        disposeOnDestroy(getDataFromDb)
        viewState?.showState(State.HasData)
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
    private fun handleError(th: Throwable, method: String) {
        viewState!!.showState(State.NetworkError)
        loadWeatherFromDb()
        loadForecastFromDb()
        loadAQIFromDb()
        Log.e(PRESENTER_WEATHER_TAG, "handleError " + method + " " + th.message, th)
    }

    private fun loadWeatherFromDb() {
        val loadFromDb = Single.fromCallable { ConverterOpenWeather.getLastData(context) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<WeatherEntity>? ->
                            weatherEntity = data
                            updateWeather()
                        }) { th: Throwable -> handleDbError(th, "loadWeatherFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.d(PRESENTER_WEATHER_TAG, "Load WeatherData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadForecastFromDb() {
        val loadFromDb = Single.fromCallable { ConverterOpenForecast.getLastData(context) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<ForecastEntity>? ->
                            forecastEntity = data
                            updateWeather()
                        }) { th: Throwable -> handleDbError(th, "loadForecastFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.d(PRESENTER_WEATHER_TAG, "Load ForecastData from db")
        viewState!!.showState(State.HasData)
    }

    private fun loadAQIFromDb() {
        val loadFromDb = Single.fromCallable { ConverterAqi.getLastData(context) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data: List<AqiEntity>? ->
                            aqiEntity = data
                            updateAqi()
                        }) { th: Throwable -> handleDbError(th,"loadAQIFromDb") }
        disposeOnDestroy(loadFromDb)
        Log.d(PRESENTER_WEATHER_TAG, "Load AQIData from db")
        viewState!!.showState(State.HasData)
    }

    private fun handleDbError(th: Throwable, method: String) {
        viewState!!.showState(State.DbError)
        Log.e(PRESENTER_WEATHER_TAG, "handleDbError " + method + " " + th.message, th)
    }

    init {
        mIsGps = isGps
        mSearch = isSearch
        mSearchLocation = location
    }
}