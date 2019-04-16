package com.iaruchkin.deepbreath.room.daos;

import com.iaruchkin.deepbreath.room.entities.CityEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CityDao {

    @Query("SELECT * FROM cities")
    List<CityEntity> getAll();

    @Query("SELECT * FROM cities WHERE parameter = :parameter")
    List<CityEntity> getByParameter(String parameter);

    @Query("SELECT * FROM cities WHERE id = :id")
    CityEntity getDataById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CityEntity... locations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CityEntity location);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(CityEntity location);

    @Delete
    void delete(CityEntity location);

    @Query("DELETE FROM cities")
    void deleteAll();

    @Query("DELETE FROM cities WHERE id = :id")
    void deleteById(String id);

}
