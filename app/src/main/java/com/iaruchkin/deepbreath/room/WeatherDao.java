package com.iaruchkin.deepbreath.room;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM aqi")
    List<WeatherEntity> getAll();

    @Query("SELECT * FROM aqi WHERE location = :location")
    List<WeatherEntity> getAll(String location);

    @Query("SELECT * FROM aqi WHERE id = :id")
    WeatherEntity getNewsById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(WeatherEntity... weatherEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherEntity weatherEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(WeatherEntity weatherEntity);

    @Delete
    void delete(WeatherEntity weatherEntity);

    @Query("DELETE FROM aqi WHERE location = :location")
    void deleteAll(String location);

}
