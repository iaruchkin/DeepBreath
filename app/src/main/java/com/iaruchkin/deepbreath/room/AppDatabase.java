package com.iaruchkin.deepbreath.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.iaruchkin.deepbreath.room.daos.AqiDao;
import com.iaruchkin.deepbreath.room.daos.CityDao;
import com.iaruchkin.deepbreath.room.daos.ConditionDao;
import com.iaruchkin.deepbreath.room.daos.FavoritesDao;
import com.iaruchkin.deepbreath.room.daos.ForecastDao;
import com.iaruchkin.deepbreath.room.daos.WeatherDao;
import com.iaruchkin.deepbreath.room.entities.AqiEntity;
import com.iaruchkin.deepbreath.room.entities.CityEntity;
import com.iaruchkin.deepbreath.room.entities.ConditionEntity;
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity;
import com.iaruchkin.deepbreath.room.entities.ForecastEntity;
import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

@Database(entities = {ForecastEntity.class, AqiEntity.class, WeatherEntity.class, ConditionEntity.class, CityEntity.class, FavoritesEntity.class}, version = 14)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mSingleton;

    private static final String DATABASE_NAME = "DeepBreathRoom.db";

    public abstract ForecastDao forecastDao();
    public abstract AqiDao aqiDao();
    public abstract WeatherDao weatherDao();
    public abstract ConditionDao conditionDao();
    public abstract CityDao cityDao();
    public abstract FavoritesDao favoritesDao();

    public static AppDatabase getAppDatabase(Context context){
        if (mSingleton == null){
            synchronized (AppDatabase.class){
                if (mSingleton == null){
                    mSingleton = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .fallbackToDestructiveMigration()//if need to migrate then drop database
                            .build();
                }
            }
        }
        return mSingleton;
    }
}