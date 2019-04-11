package com.iaruchkin.deepbreath.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ForecastEntity.class, AqiEntity.class, WeatherEntity.class, ConditionEntity.class, CityEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mSingleton;

    private static final String DATABASE_NAME = "DeepBreathRoom.db";

    public abstract ForecastDao forecastDao();
    public abstract AqiDao aqiDao();
    public abstract WeatherDao weatherDao();
    public abstract ConditionDao conditionDao();
    public abstract CityDao cityDao();

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