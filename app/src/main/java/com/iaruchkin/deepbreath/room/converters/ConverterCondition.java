package com.iaruchkin.deepbreath.room.converters;

import android.content.Context;
import android.util.Log;

import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO.OfflineCondition.Language;
import com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO.OfflineCondition.WeatherCondition;
import com.iaruchkin.deepbreath.room.AppDatabase;
import com.iaruchkin.deepbreath.room.daos.ConditionDao;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;

import java.util.ArrayList;
import java.util.List;

public class ConverterCondition {

    private static final String TAG = "RoomConverterCondition";

    private static ConditionDao conditionDao = AppDatabase.getAppDatabase(App.INSTANCE).conditionDao();

    public static List<ConditionEntity> dtoToDao(List<WeatherCondition> listDto, int lang){

        List<ConditionEntity> listDao = new ArrayList<>();
        for (WeatherCondition dto : listDto) {
            ConditionEntity conditionEntity = new ConditionEntity();

            conditionEntity.setLang(lang);

            conditionEntity.setCode(dto.getCode());
            conditionEntity.setIcon(dto.getIcon());
            conditionEntity.setDay(dto.getDay());
            conditionEntity.setNight(dto.getNight());

            if(lang < dto.getLanguages().size()){
                Language language = dto.getLanguages().get(lang);

                conditionEntity.setId(dto.getCode() + language.getLangIso());

                conditionEntity.setLangName(language.getLangName());
                conditionEntity.setLangIso(language.getLangIso());
                conditionEntity.setDayText(language.getDayText());
                conditionEntity.setNightText(language.getNightText());
            }else {
                conditionEntity.setId(dto.getCode() + "en");

                conditionEntity.setLangName("English");
                conditionEntity.setLangIso("en");
                conditionEntity.setDayText(dto.getDay());
                conditionEntity.setNightText(dto.getNight());

            }
            listDao.add(conditionEntity);
            Log.i(TAG, conditionEntity.toString());
        }
        return listDao;
    }

    public static ConditionEntity getDataById(Context context, String id){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        return db.conditionDao().getDataById(id);
    }
    public static List<ConditionEntity> getDataByLang(Context context, Integer lang) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Condition data loaded from DB");
        return db.conditionDao().getDataByLang(lang);
    }

    public static List<ConditionEntity> loadDataFromDb(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        Log.i(TAG, "Condition data loaded from DB");
        return db.conditionDao().getAll();
    }

    public static void saveAllDataToDb(Context context, List<ConditionEntity> list){
        AppDatabase db = AppDatabase.getAppDatabase(context);
        db.conditionDao().deleteAll();
        Log.i(TAG, "Condition DB: deleteAll");

        ConditionEntity data[] = list.toArray(new ConditionEntity[list.size()]);
        db.conditionDao().insertAll(data);
        Log.i(TAG, "Condition DB: insertAll");

        Log.i(TAG, "Condition data saved to DB");
        conditionDao.insertAll(data);
    }
}
