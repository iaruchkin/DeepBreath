package com.iaruchkin.deepbreath.room;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.AqiResponse;
import com.iaruchkin.deepbreath.network.aqicnDTO.Data;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Forecastday;

import java.util.ArrayList;
import java.util.List;

public class ConverterAqi {

    private static final String TAG = "RoomConverterAQI";

    private static AqiDao aqiDao = AppDatabase.getAppDatabase(App.INSTANCE).aqiDao();

    public static AqiEntity dtoToDao(Data aqiDTO, String weatherLocation){

        AqiEntity aqiEntity = new AqiEntity();
        aqiEntity.setId(aqiDTO.getTime().getS()+aqiDTO.getCity().getName());
        aqiEntity.setLocation(aqiDTO.getCity().getName());
        aqiEntity.setAqi(aqiDTO.getAqi().toString());
        aqiEntity.setDate(aqiDTO.getTime().getS());
        aqiEntity.setPm10(aqiDTO.getIaqi().getPm10().getV());

        return aqiEntity;
    }

    public static AqiEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.aqiDao().getDataById(id);
    }

    public static AqiEntity loadDataFromDb(Context context, String location) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.aqiDao().getAll(location);//todo think about request
    }

    public static void saveAllDataToDb(Context context, AqiEntity data, String location){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.aqiDao().deleteAll(location);
        Log.i(TAG, "AQI DB: deleteAll");

//        AqiEntity data[] = list.toArray(new WeatherEntity[list.size()]);
        db.aqiDao().insertAll(data);
        Log.i(TAG, "AQI DB: insertAll");

        Log.i(TAG, "AQI data saved to DB");
        Log.i(TAG, data.toString());
        aqiDao.insertAll(data);

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
