package com.iaruchkin.deepbreath.room.converters

import android.content.Context
import android.util.Log
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.network.dtos.OpenWeatherResponse
import com.iaruchkin.deepbreath.room.AppDatabase
import com.iaruchkin.deepbreath.room.entities.ForecastEntity
import com.iaruchkin.deepbreath.utils.StringUtils
import java.util.*

object ConverterOpenForecast {

    private val TAG = "RoomConverterForecast"

    private val forecastDao = AppDatabase.getAppDatabase(App.INSTANCE).forecastDao()

    fun dtoToDao(weatherDTO: OpenWeatherResponse, weatherLocation: String): List<ForecastEntity> {
        val listDTO = weatherDTO.list
        val listDao = ArrayList<ForecastEntity>()
//        val list = ArrayList<ForecastEntity>()
//
//        val list2 = mutableListOf<X>()
//        for (i in listDTO.indices step 8){
//            var max = 0.0
//            var min = 0.0
//            for(j in 0..7){
//                val a = listDTO[j].main.tempMax
//                if (max>a) max = a
//                val b = listDTO[j].main.tempMin
//                if (min>b) min = b
//            }
//            list2.add(listDTO[i])
//        }

        var i = 7
        var max = 0.0
        var min = 1000.0

        for (dto in listDTO) {
            val forecastEntity = ForecastEntity()

            //id
            forecastEntity.id = dto.dtTxt + weatherDTO.city.name
            forecastEntity.autoid = dto.dt.toLong()

            //geo
            forecastEntity.parameter = weatherLocation

            //location
            forecastEntity.locationName = weatherDTO.city.name
            forecastEntity.locationRegion = weatherDTO.city.country
            forecastEntity.locationCountry = weatherDTO.city.country
            forecastEntity.locationTz_id = weatherDTO.city.timezone.toString()
            forecastEntity.locationLat = weatherDTO.city.coord.lat
            forecastEntity.locationLon = weatherDTO.city.coord.lon
            forecastEntity.locationLocaltime = weatherDTO.city.timezone.toString() //todo fix
            forecastEntity.locationLocaltime_epoch = weatherDTO.city.timezone

            //date
            forecastEntity.date = Date(dto.dt.toLong())
            forecastEntity.date_epoch = dto.dt
            forecastEntity.isDay = if (dto.weather[0].icon.contains("d")) 1 else 0

            //weather metric
            forecastEntity.maxtemp_c = dto.main.tempMax.inCelsius()
            forecastEntity.avgtemp_c = dto.main.temp.inCelsius()
            forecastEntity.mintemp_c = dto.main.tempMin.inCelsius()
            forecastEntity.maxwind_mph = dto.wind.speed.inMph()
//            forecastEntity.totalprecip_mm = dto.getDay().getTotalprecipMm()
            forecastEntity.wind_degree = dto.wind.deg.toInt()

            //condition
            forecastEntity.conditionText = dto.weather[0].main
            forecastEntity.conditionCode = dto.weather[0].id

            //astro
            forecastEntity.sunrise = StringUtils.formatDate(weatherDTO.city.sunrise.toLong(), "HH:mm")
            forecastEntity.sunset = StringUtils.formatDate(weatherDTO.city.sunset.toLong(), "HH:mm")
            forecastEntity.moonrise = "" //todo fix
            forecastEntity.moonset = ""

            //weather imperial
            forecastEntity.maxtemp_f = dto.main.tempMax.inFahrenheit()
            forecastEntity.avgtemp_f = dto.main.tempMax.inFahrenheit()
            forecastEntity.mintemp_f = dto.main.tempMax.inFahrenheit()
            forecastEntity.maxwind_kph = dto.wind.speed.inKph()
//            forecastEntity.totalprecip_in = dto.getDay().getTotalprecipIn()


            if (i <= 7) {
                val a = dto.main.tempMax
                if (a > max) max = a
                val b = dto.main.tempMin
                if (b < min) min = b
            }

            if (i == 7) {
                forecastEntity.maxtemp_c = max.inCelsius()
                forecastEntity.mintemp_c = min.inCelsius()
                listDao.add(forecastEntity)
                i = 0
                max = 0.0
                min = 1000.0
            }
            i++
        }
        Log.w(TAG, listDao.toString())

        return listDao
    }

    fun getDataById(context: Context, id: String): ForecastEntity {
        val db = AppDatabase.getAppDatabase(context)
        return db.forecastDao().getDataById(id)
    }

    fun getDataByParameter(context: Context, parameter: String): List<ForecastEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "Weather data loaded from DB")
        return db.forecastDao().getByParameter(parameter)
    }

    fun getLastData(context: Context): List<ForecastEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "Weather data loaded from DB")
        return db.forecastDao().last
    }

    fun getDataByLocation(context: Context, location: String): List<ForecastEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "Weather data loaded from DB")
        return db.forecastDao().getAll(location)
    }

    fun loadDataFromDb(context: Context): List<ForecastEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "Weather data loaded from DB")
        return db.forecastDao().all
    }

    fun saveAllDataToDb(context: Context, list: List<ForecastEntity>, location: String) {
        val db = AppDatabase.getAppDatabase(context)
        db.forecastDao().deleteAll(location)
        Log.i(TAG, "Weather DB: deleteAll")

        val data = list.toTypedArray()
        db.forecastDao().insertAll(*data)
        Log.i(TAG, "Weather DB: insertAll")

        Log.i(TAG, "Weather data saved to DB")
        forecastDao.insertAll(*data)
    }

    fun Double.inCelsius(): Double = this - 273.15
    fun Double.inFahrenheit(): Double = (this - 273.15) * 9 / 5 + 32
    fun Double.inKph(): Double = this * 3.6
    fun Double.inMph(): Double = this * 2.236934
}
