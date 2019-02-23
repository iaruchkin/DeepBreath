package com.iaruchkin.deepbreath.room;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.aqicnDTO.Data;

import java.util.ArrayList;
import java.util.List;

public class ConverterData {

    private static final String TAG = "RoomConverter";

    private static WeatherDao weatherDao = AppDatabase.getAppDatabase(App.INSTANCE).weatherDao();

    public static List<WeatherEntity> dtoToDao(Data data, String weatherLocation){
        List<WeatherEntity> listDao = new ArrayList<>();

            WeatherEntity weatherEntity = new WeatherEntity();
            weatherEntity.setId(data.getDominentpol()+weatherLocation);
            weatherEntity.setLocation(weatherLocation);
            weatherEntity.setAqi(data.getAqi().toString());
            weatherEntity.setDate(data.getTime().getS());

            listDao.add(weatherEntity);

        return listDao;
    }

    public static WeatherEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.weatherDao().getNewsById(id);
    }

    public static List<WeatherEntity> loadDataFromDb(Context context, String location) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "data loaded from DB");
        return db.weatherDao().getAll(location);
    }

    public static void saveAllDataToDb(Context context, List<WeatherEntity> list, String location){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.weatherDao().deleteAll(location);
        Log.i(TAG, "DB: deleteAll");

        WeatherEntity data[] = list.toArray(new WeatherEntity[list.size()]);
        db.weatherDao().insertAll(data);
        Log.i(TAG, "DB: insertAll");

        Log.i(TAG, "data saved to DB");
        Log.i(TAG, list.toString());
        weatherDao.insertAll(data);

    }

    public static void editNewsToDb(Context context, WeatherEntity weatherEntity){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.weatherDao().edit(weatherEntity);
    }

    public static void deleteNewsFromDb(Context context, WeatherEntity weatherEntity){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.weatherDao().delete(weatherEntity);
    }
}
