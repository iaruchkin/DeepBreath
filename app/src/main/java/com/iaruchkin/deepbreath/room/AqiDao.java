package com.iaruchkin.deepbreath.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AqiDao {

    @Query("SELECT * FROM aqi")
    AqiEntity getAll();

    @Query("SELECT * FROM aqi WHERE location = :location")
    AqiEntity getAll(String location);

    @Query("SELECT * FROM aqi WHERE id = :id")
    AqiEntity getDataById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AqiEntity aqiEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(AqiEntity... aqiEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(AqiEntity aqiEntity);

    @Delete
    void delete(AqiEntity aqiEntity);

    @Query("DELETE FROM aqi WHERE location = :location")
    void deleteAll(String location);

}
