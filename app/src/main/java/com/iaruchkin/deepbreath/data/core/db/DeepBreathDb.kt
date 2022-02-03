package com.iaruchkin.deepbreath.data.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iaruchkin.deepbreath.room.daos.*
import com.iaruchkin.deepbreath.room.entities.*
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(
    entities = [
        ForecastEntity::class,
        AqiEntity::class,
        WeatherEntity::class,
        ConditionEntity::class,
        FavoritesEntity::class],
    version = 15,
    exportSchema = true
)

abstract class DeepBreathDb : RoomDatabase() {

    abstract fun forecastDao(): ForecastDao
    abstract fun aqiDao(): AqiDao
    abstract fun weatherDao(): WeatherDao
    abstract fun conditionDao(): ConditionDao
    abstract fun favoritesDao(): FavoritesDao

    companion object {

        fun create(@ApplicationContext appContext: Context): DeepBreathDb =
            Room.databaseBuilder(
                appContext,
                DeepBreathDb::class.java,
                "DeepBreathRoom.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}