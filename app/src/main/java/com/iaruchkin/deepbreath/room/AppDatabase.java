package com.iaruchkin.deepbreath.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mSingleton;

    private static final String DATABASE_NAME = "AqiRoomDb.db";

    public abstract WeatherDao weatherDao();

    public static AppDatabase getAppDatabase(Context context){
        if (mSingleton == null){
            synchronized (AppDatabase.class){
                if (mSingleton == null){
                    mSingleton = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .build();
                }
            }
        }
        return mSingleton;
    }
}
