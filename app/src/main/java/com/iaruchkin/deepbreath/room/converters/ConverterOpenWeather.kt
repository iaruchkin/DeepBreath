package com.iaruchkin.deepbreath.room.converters

import android.content.Context
import android.util.Log
import com.iaruchkin.deepbreath.App
import com.iaruchkin.deepbreath.network.dtos.OpenWeatherResponse
import com.iaruchkin.deepbreath.room.AppDatabase
import com.iaruchkin.deepbreath.room.entities.WeatherEntity
import java.util.*

object ConverterOpenWeather {

    private val TAG = "RoomConverterWeather"

    private val weatherDao = AppDatabase.getAppDatabase(App.INSTANCE).weatherDao()

    fun dtoToDao(weatherDTO: OpenWeatherResponse, weatherLocation: String): List<WeatherEntity> {

        val listDao = ArrayList<WeatherEntity>()
        val weatherEntity = WeatherEntity()
        val dto = weatherDTO.list[0]

        //id
        weatherEntity.id = dto.dtTxt + weatherDTO.city.name
        weatherEntity.autoid = dto.dt.toLong()
        Log.e("time", dto.dt.toString())

        //geo
        weatherEntity.parameter = weatherLocation

        //location
        weatherEntity.location = weatherDTO.city.name
        weatherEntity.country = weatherDTO.city.country
        weatherEntity.region = weatherDTO.city.country

        //time
        weatherEntity.last_updated = dto.dtTxt
        weatherEntity.last_updated_epoch = dto.dt
        weatherEntity.isDay = 1 //todo fix

        //weather metric
        weatherEntity.temp_c = dto.main.temp.inCelsius()
        weatherEntity.feelslike_c = dto.main.temp.inCelsius() //todo fix
        weatherEntity.pressure_mb = dto.main.pressure
//        weatherEntity.precip_mm = weatherDTO.getCurrent().getPrecipMm()
        weatherEntity.wind_mph = dto.wind.speed.inMph()
        weatherEntity.wind_degree = dto.wind.deg.toInt()
        weatherEntity.wind_dir = dto.wind.deg.toString()
        weatherEntity.cloud = dto.clouds.all //todo fix
        weatherEntity.humidity = dto.main.humidity

        //weather imperial
        weatherEntity.temp_f = dto.main.temp.inFahrenheit()
        weatherEntity.feelslike_f = dto.main.temp.inFahrenheit()
        weatherEntity.pressure_in = dto.main.pressure.inInch()
//        weatherEntity.precip_in = weatherDTO.getCurrent().getPrecipIn()
        weatherEntity.wind_kph = dto.wind.speed.inKph()

        //condition
        weatherEntity.conditionText = dto.weather[0].description
        weatherEntity.conditionCode = dto.weather[0].id

        listDao.add(weatherEntity)
        Log.w(TAG, weatherEntity.toString())

        return listDao
    }

    fun getDataById(context: Context, id: String): WeatherEntity {
        val db = AppDatabase.getAppDatabase(context)
        return db.weatherDao().getDataById(id)
    }

    fun getDataByParameter(context: Context, parameter: String): List<WeatherEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "AQI data loaded from DB")
        return db.weatherDao().getByParameter(parameter)
    }

    fun getLastData(context: Context): List<WeatherEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "AQI data loaded from DB")
        return db.weatherDao().last
    }

    fun getDataByLocation(context: Context, location: String): List<WeatherEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "AQI data loaded from DB")
        return db.weatherDao().getAll(location)
    }

    fun loadDataFromDb(context: Context): List<WeatherEntity> {
        val db = AppDatabase.getAppDatabase(context)
        Log.i(TAG, "AQI data loaded from DB")
        return db.weatherDao().all
    }

    fun saveAllDataToDb(context: Context, list: List<WeatherEntity>, parameter: String) {
        val db = AppDatabase.getAppDatabase(context)
        db.weatherDao().deleteAll(parameter)
        Log.i(TAG, "AQI DB: deleteAll")

        val data = list.toTypedArray()
        db.weatherDao().insertAll(*data)
        Log.i(TAG, "AQI DB: insertAll")

        Log.i(TAG, "AQI data saved to DB")
        weatherDao.insertAll(*data)
    }

    fun Double.inCelsius(): Double = this - 273.15
    fun Double.inFahrenheit(): Double = (this - 273.15) * 9 / 5 + 32
    fun Double.inKph(): Double = this * 3.6
    fun Double.inMph(): Double = this * 2.236934
    fun Double.inInch(): Double = this * 0.030
}
