package com.iaruchkin.deepbreath.room;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.aqicnDTO.Data;

import java.util.ArrayList;
import java.util.List;

public class ConverterNews {

    private static final String TAG = "RoomConverter";

    private static WeatherDao weatherDao = AppDatabase.getAppDatabase(App.INSTANCE).weatherDao();

    public static List<WeatherEntity> dtoToDao(Data data, String weatherLocation){
        List<WeatherEntity> listDao = new ArrayList<>();

            WeatherEntity weatherEntity = new WeatherEntity();
            weatherEntity.setId(data.getDominentpol()+weatherLocation);
            weatherEntity.setLocation(weatherLocation);
            weatherEntity.setAqi(data.getAqi().toString());
            weatherEntity.setDate(data.getTime().toString());

            listDao.add(weatherEntity);

        return listDao;
    }

    public static WeatherEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.weatherDao().getNewsById(id);
    }

    public static List<WeatherEntity> loadDataFromDb(Context context, String category) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "data loaded from DB");
        return db.weatherDao().getAll(category);
    }

    public static void saveAllDataToDb(Context context, List<WeatherEntity> list, String category){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.weatherDao().deleteAll(category);
        Log.i(TAG, "DB: deleteAll");

        WeatherEntity news[] = list.toArray(new WeatherEntity[list.size()]);
        db.weatherDao().insertAll(news);
        Log.i(TAG, "DB: insertAll");

        Log.i(TAG, "data saved to DB");
        Log.i(TAG, list.toString());
        weatherDao.insertAll(news);

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
