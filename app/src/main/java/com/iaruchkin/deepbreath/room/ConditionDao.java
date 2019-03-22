package com.iaruchkin.deepbreath.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ConditionDao {

    @Query("SELECT * FROM condition")
    List<ConditionEntity> getAll();

    @Query("SELECT * FROM condition WHERE id = :id")
    ConditionEntity getDataById(String id);

    @Query("SELECT * FROM condition WHERE lang = :lang")
    List<ConditionEntity> getDataByLang(Integer lang);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConditionEntity conditionEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ConditionEntity... conditionEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void edit(ConditionEntity conditionEntity);

    @Delete
    void delete(ConditionEntity conditionEntity);

    @Query("DELETE FROM condition WHERE code = :code")
    void deleteAll(Integer code);

    @Query("DELETE FROM condition")
    void deleteAll();

}
