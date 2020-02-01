package com.iaruchkin.deepbreath.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.iaruchkin.deepbreath.room.entities.WeatherEntity;

import java.util.List;

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather")
    List<WeatherEntity> getAll();

    @Query("SELECT * FROM weather WHERE location = :location")
    List<WeatherEntity> getAll(String location);

    @Query("SELECT * FROM weather WHERE parameter = :parameter")
    List<WeatherEntity> getByParameter(String parameter);

    @Query("SELECT * FROM weather WHERE id = :id")
    WeatherEntity getDataById(String id);

    @Query("SELECT * FROM weather ORDER BY autoid DESC LIMIT 1")
    List<WeatherEntity> getLast();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(WeatherEntity... weatherEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherEntity weatherEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(WeatherEntity weatherEntity);

    @Delete
    void delete(WeatherEntity weatherEntity);

    @Query("DELETE FROM weather WHERE parameter = :parameter")
    void deleteAll(String parameter);

    @Query("DELETE FROM weather WHERE location = :location")
    void deleteByLocation(String location);

}
