package com.iaruchkin.deepbreath.room.daos;

import com.iaruchkin.deepbreath.room.entities.AqiEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AqiDao {

    @Query("SELECT * FROM aqi")
    List<AqiEntity> getAll();

    @Query("SELECT * FROM aqi WHERE idx = :idx")
    List<AqiEntity> getAll(String idx);

    @Query("SELECT * FROM aqi WHERE parameter = :parameter")
    List<AqiEntity> getByParameter(String parameter);

    @Query("SELECT * FROM aqi WHERE id = :id")
    AqiEntity getDataById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(AqiEntity... aqiEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AqiEntity aqiEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(AqiEntity aqiEntity);

    @Delete
    void delete(AqiEntity aqiEntity);

    @Query("DELETE FROM aqi WHERE parameter = :parameter")
    void deleteAll(String parameter);

    @Query("DELETE FROM aqi WHERE cityName = :cityName")
    void deleteByLocation(String cityName);

}
