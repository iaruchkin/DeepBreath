package com.iaruchkin.deepbreath.room;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.WeatherResponse;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Forecastday;

import java.util.ArrayList;
import java.util.List;

public class ConverterForecast {

    private static final String TAG = "RoomConverterForecast";

    private static ForecastDao forecastDao = AppDatabase.getAppDatabase(App.INSTANCE).forecastDao();

    public static List<ForecastEntity> dtoToDao(WeatherResponse weatherDTO, String weatherLocation){
        List<Forecastday> listDTO = weatherDTO.getForecast().getForecastday();
        List<ForecastEntity> listDao = new ArrayList<>();

        for(Forecastday dto : listDTO) {
            ForecastEntity forecastEntity = new ForecastEntity();
                //id
                forecastEntity.setId(dto.getDate() + weatherDTO.getLocation().getName());

                //geo
                forecastEntity.setParameter(weatherLocation);

                //location
                forecastEntity.setLocationName(weatherDTO.getLocation().getName());
                forecastEntity.setLocationRegion(weatherDTO.getLocation().getRegion());
                forecastEntity.setLocationCountry(weatherDTO.getLocation().getCountry());
                forecastEntity.setLocationTz_id(weatherDTO.getLocation().getTzId());
                forecastEntity.setLocationLat(weatherDTO.getLocation().getLat());
                forecastEntity.setLocationLon(weatherDTO.getLocation().getLong());
                forecastEntity.setLocationLocaltime(weatherDTO.getLocation().getLocaltime());
                forecastEntity.setLocationLocaltime_epoch(weatherDTO.getLocation().getLocaltimeEpoch());

                //date
                forecastEntity.setDate(dto.getDate());
                forecastEntity.setDate_epoch(dto.getDateEpoch());
                forecastEntity.setIsDay(weatherDTO.getCurrent().getIsDay());


            //weather metric
                forecastEntity.setMaxtemp_c(dto.getDay().getMaxtempC());
                forecastEntity.setAvgtemp_c(dto.getDay().getAvgtempC());
                forecastEntity.setMintemp_c(dto.getDay().getMintempC());
                forecastEntity.setMaxwind_mph(dto.getDay().getMaxwindMph());
                forecastEntity.setTotalprecip_mm(dto.getDay().getTotalprecipMm());

                //condition
                forecastEntity.setConditionText(dto.getDay().getCondition().getText());
                forecastEntity.setConditionCode(dto.getDay().getCondition().getCode());

                //astro
                forecastEntity.setSunrise(dto.getAstro().getSunrise());
                forecastEntity.setSunset(dto.getAstro().getSunset());
                forecastEntity.setMoonrise(dto.getAstro().getMoonrise());
                forecastEntity.setMoonset(dto.getAstro().getMoonset());

                //weather imperial
                forecastEntity.setMaxtemp_f(dto.getDay().getMaxtempF());
                forecastEntity.setAvgtemp_f(dto.getDay().getAvgtempF());
                forecastEntity.setMintemp_f(dto.getDay().getMintempF());
                forecastEntity.setMaxwind_kph(dto.getDay().getMaxwindKph());
                forecastEntity.setTotalprecip_in(dto.getDay().getTotalprecipIn());

            listDao.add(forecastEntity);
        }
        Log.w(TAG, listDao.toString());

        return listDao;
    }

    public static ForecastEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.forecastDao().getDataById(id);
    }

    public static List<ForecastEntity> getDataByParameter(Context context, String parameter) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Weather data loaded from DB");
        return db.forecastDao().getByParameter(parameter); //todo think about request
    }

    public static List<ForecastEntity> getDataByLocation(Context context, String location) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Weather data loaded from DB");
        return db.forecastDao().getAll(location); //todo think about request
    }

    public static List<ForecastEntity> loadDataFromDb(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Weather data loaded from DB");
        return db.forecastDao().getAll(); //todo think about request
    }

    public static void saveAllDataToDb(Context context, List<ForecastEntity> list, String location){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.forecastDao().deleteAll(location);
        Log.i(TAG, "Weather DB: deleteAll");

        ForecastEntity data[] = list.toArray(new ForecastEntity[list.size()]);
        db.forecastDao().insertAll(data);
        Log.i(TAG, "Weather DB: insertAll");

        Log.i(TAG, "Weather data saved to DB");
        forecastDao.insertAll(data);
    }
}
