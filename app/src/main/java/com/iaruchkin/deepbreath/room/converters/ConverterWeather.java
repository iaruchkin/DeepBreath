package com.iaruchkin.deepbreath.room.converters;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.dtos.WeatherResponse;
import com.iaruchkin.deepbreath.room.AppDatabase;
import com.iaruchkin.deepbreath.room.daos.WeatherDao;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import java.util.ArrayList;
import java.util.List;

public class ConverterWeather {

    private static final String TAG = "RoomConverterWeather";

    private static WeatherDao weatherDao = AppDatabase.getAppDatabase(App.INSTANCE).weatherDao();

    public static List<WeatherEntity> dtoToDao(WeatherResponse weatherDTO, String weatherLocation){

        List<WeatherEntity> listDao = new ArrayList<>();
        WeatherEntity weatherEntity = new WeatherEntity();

            //id
            weatherEntity.setId(weatherDTO.getCurrent().getLastUpdateEpoch() + weatherDTO.getLocation().getName());
        weatherEntity.setAutoid(weatherDTO.getCurrent().getLastUpdateEpoch());
        Log.e("time", String.valueOf(weatherDTO.getCurrent().getLastUpdateEpoch()));

        //geo
            weatherEntity.setParameter(weatherLocation);

            //location
            weatherEntity.setLocation(weatherDTO.getLocation().getName());
            weatherEntity.setCountry(weatherDTO.getLocation().getCountry());
            weatherEntity.setRegion(weatherDTO.getLocation().getRegion());

        //time
            weatherEntity.setLast_updated(weatherDTO.getCurrent().getLastUpdated());
            weatherEntity.setLast_updated_epoch(weatherDTO.getCurrent().getLastUpdateEpoch());
            weatherEntity.setIsDay(weatherDTO.getCurrent().getIsDay());

        //weather metric
            weatherEntity.setTemp_c(weatherDTO.getCurrent().getTempC());
            weatherEntity.setFeelslike_c(weatherDTO.getCurrent().getFeelslikeC());
            weatherEntity.setPressure_mb(weatherDTO.getCurrent().getPressureMb());
            weatherEntity.setPrecip_mm(weatherDTO.getCurrent().getPrecipMm());
            weatherEntity.setWind_mph(weatherDTO.getCurrent().getWindMph());
            weatherEntity.setWind_degree(weatherDTO.getCurrent().getWindDegree());
            weatherEntity.setWind_dir(weatherDTO.getCurrent().getWindDir());
            weatherEntity.setCloud(weatherDTO.getCurrent().getCloud());
            weatherEntity.setHumidity(weatherDTO.getCurrent().getHumidity());

            //weather imperial
            weatherEntity.setTemp_f(weatherDTO.getCurrent().getTempF());
            weatherEntity.setFeelslike_f(weatherDTO.getCurrent().getFeelslikeF());
            weatherEntity.setPressure_in(weatherDTO.getCurrent().getPressureIn());
            weatherEntity.setPrecip_in(weatherDTO.getCurrent().getPrecipIn());
            weatherEntity.setWind_kph(weatherDTO.getCurrent().getWindKph());

            //condition
            weatherEntity.setConditionText(weatherDTO.getCurrent().getCondition().getText());
            weatherEntity.setConditionCode(weatherDTO.getCurrent().getCondition().getCode());

        listDao.add(weatherEntity);
        Log.w(TAG, weatherEntity.toString());

        return listDao;
    }

    public static WeatherEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.weatherDao().getDataById(id);
    }

    public static List<WeatherEntity> getDataByParameter(Context context, String parameter) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.weatherDao().getByParameter(parameter);
    }

    public static List<WeatherEntity> getLastData(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.weatherDao().getLast();
    }

    public static List<WeatherEntity> getDataByLocation(Context context, String location) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.weatherDao().getAll(location);
    }

    public static List<WeatherEntity> loadDataFromDb(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.weatherDao().getAll();
    }

    public static void saveAllDataToDb(Context context, List<WeatherEntity> list, String parameter){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.weatherDao().deleteAll(parameter);
        Log.i(TAG, "AQI DB: deleteAll");

        WeatherEntity data[] = list.toArray(new WeatherEntity[list.size()]);
        db.weatherDao().insertAll(data);
        Log.i(TAG, "AQI DB: insertAll");

        Log.i(TAG, "AQI data saved to DB");
        weatherDao.insertAll(data);
    }
}
