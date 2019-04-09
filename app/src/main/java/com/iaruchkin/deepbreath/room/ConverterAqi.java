package com.iaruchkin.deepbreath.room;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.aqicnDTO.Data;

import java.util.ArrayList;
import java.util.List;

public class ConverterAqi {

    private static final String TAG = "RoomConverterAQI";

    private static AqiDao aqiDao = AppDatabase.getAppDatabase(App.INSTANCE).aqiDao();

    public static List<AqiEntity> dtoToDao(Data aqiDTO, String parameter){

        List<AqiEntity> listDao = new ArrayList<>();
        AqiEntity aqiEntity = new AqiEntity();

            aqiEntity.setId(aqiDTO.getIdx().toString() + aqiDTO.getTime().getS());
            aqiEntity.setIdx(aqiDTO.getIdx().toString());

            //parameter
            aqiEntity.setParameter(parameter);

            aqiEntity.setCityGeo(aqiDTO.getCity().getGeo().toString());
            aqiEntity.setCityName(aqiDTO.getCity().getName());
            aqiEntity.setCityUrl(aqiDTO.getCity().getUrl());

            aqiEntity.setAqi(aqiDTO.getAqi());

            if(aqiDTO.getIaqi().getPm10() != null) aqiEntity.setPm10(aqiDTO.getIaqi().getPm10().getV());
            if(aqiDTO.getIaqi().getPm25() != null) aqiEntity.setPm25(aqiDTO.getIaqi().getPm25().getV());

            if(aqiDTO.getIaqi().getCo() != null) aqiEntity.setCo(aqiDTO.getIaqi().getCo().getV());
            if(aqiDTO.getIaqi().getNo2() != null) aqiEntity.setNo2(aqiDTO.getIaqi().getNo2().getV());
            if(aqiDTO.getIaqi().getSo2() != null) aqiEntity.setSo2(aqiDTO.getIaqi().getSo2().getV());
            if(aqiDTO.getIaqi().getO3() != null) aqiEntity.setO3(aqiDTO.getIaqi().getO3().getV());

            if(aqiDTO.getIaqi().getWg() != null)aqiEntity.setWg(aqiDTO.getIaqi().getWg().getV());
            if(aqiDTO.getIaqi().getH() != null)aqiEntity.setH(aqiDTO.getIaqi().getH().getV());
            if(aqiDTO.getIaqi().getW() != null)aqiEntity.setW(aqiDTO.getIaqi().getW().getV());
            if(aqiDTO.getIaqi().getP() != null)aqiEntity.setP(aqiDTO.getIaqi().getP().getV());

            aqiEntity.setDate(aqiDTO.getTime().getS());
            aqiEntity.setDateEpoch(aqiDTO.getTime().getV());

        listDao.add(aqiEntity);
        Log.w(TAG, aqiEntity.toString());

        return listDao;
    }

//    pm25: "PM<sub>2.5</sub>",
//    pm10: "PM<sub>10</sub>",
//    o3: "Ozone",
//    no2: "Nitrogen Dioxide",
//    so2: "Sulphur Dioxide",
//    co: "Carbon Monoxyde",
//    t: "Temperature",
//    w: "Wind",
//    r: "Rain (precipitation)",
//    h: "Relative Humidity",
//    d: "Dew",
//    p: "Atmostpheric Pressure"

    public static AqiEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.aqiDao().getDataById(id);
    }

    public static List<AqiEntity> getDataByParameter(Context context, String parameter) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.aqiDao().getByParameter(parameter);//todo think about request
    }

    public static List<AqiEntity> getDataByIdx(Context context, String idx) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.aqiDao().getAll(idx);//todo think about request
    }

    public static List<AqiEntity> loadDataFromDb(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "AQI data loaded from DB");
        return db.aqiDao().getAll();//todo think about request
    }

    public static void saveAllDataToDb(Context context, List<AqiEntity> list, String parameter){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.aqiDao().deleteAll(parameter);
        Log.i(TAG, "AQI DB: deleteAll");

        AqiEntity data[] = list.toArray(new AqiEntity[list.size()]);
        db.aqiDao().insertAll(data);
        Log.i(TAG, "AQI DB: insertAll");

        Log.i(TAG, "AQI data saved to DB");
        aqiDao.insertAll(data);
    }
}
