package com.iaruchkin.deepbreath.room.converters;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.dtos.WeatherResponse;
import com.iaruchkin.deepbreath.room.AppDatabase;
import com.iaruchkin.deepbreath.room.daos.CityDao;
import com.iaruchkin.deepbreath.room.entities.CityEntity;

import java.util.ArrayList;
import java.util.List;

//todo implement in future versions
public class ConverterCity {

    private static final String TAG = "RoomConverterForecast";

    private static CityDao cityDao = AppDatabase.getAppDatabase(App.INSTANCE).cityDao();

    public static List<CityEntity> dtoToDao(WeatherResponse weatherDTO, String weatherLocation){
        List<CityEntity> listDao = new ArrayList<>();
        CityEntity cityEntity = new CityEntity();
            //id
            cityEntity.setId(weatherDTO.getLocation().getRegion() + weatherDTO.getLocation().getName() + weatherDTO.getLocation().getCountry());

            //geo
            cityEntity.setParameter(weatherLocation);

            //location
            cityEntity.setLocation(weatherDTO.getLocation().getName());
            cityEntity.setRegion(weatherDTO.getLocation().getRegion());
            cityEntity.setCountry(weatherDTO.getLocation().getCountry());
            cityEntity.setLocationTz_id(weatherDTO.getLocation().getTzId());
            cityEntity.setLocationLat(weatherDTO.getLocation().getLat());
            cityEntity.setLocationLon(weatherDTO.getLocation().getLong());
            cityEntity.setLocationLocaltime(weatherDTO.getLocation().getLocaltime());
            cityEntity.setLocationLocaltime_epoch(weatherDTO.getLocation().getLocaltimeEpoch());

            listDao.add(cityEntity);

        Log.w(TAG, listDao.toString());

        return listDao;
    }

    public static CityEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.cityDao().getDataById(id);
    }

    public static List<CityEntity> getDataByLocation(Context context, String location) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Weather data loaded from DB");
        return db.cityDao().getByParameter(location);
    }

    public static List<CityEntity> loadDataFromDb(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Weather data loaded from DB");
        return db.cityDao().getAll();
    }

    public static void saveToDb(Context context, CityEntity cityEntity, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.cityDao().deleteById(id);
        Log.i(TAG, "City DB: delete");

        db.cityDao().insert(cityEntity);
        Log.i(TAG, "City DB: insert");

        Log.i(TAG, "City data saved to DB");
        cityDao.insert(cityEntity);
    }

    public static void saveAllDataToDb(Context context, List<CityEntity> list){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.cityDao().deleteAll();
        Log.i(TAG, "AQI DB: deleteAll");

        CityEntity data[] = list.toArray(new CityEntity[list.size()]);
        db.cityDao().insertAll(data);
        Log.i(TAG, "AQI DB: insertAll");

        Log.i(TAG, "AQI data saved to DB");
        cityDao.insertAll(data);
    }
}
