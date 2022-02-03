package com.iaruchkin.deepbreath.data.aqi.local

import androidx.room.*
import com.iaruchkin.deepbreath.room.entities.AqiEntity

@Dao
interface AqiDao {
    @get:Query("SELECT * FROM aqi")
    val all: List<AqiEntity?>?

    @Query("SELECT * FROM aqi WHERE idx = :idx")
    fun getAll(idx: String?): List<AqiEntity?>?

    @Query("SELECT * FROM aqi WHERE parameter = :parameter")
    fun getByParameter(parameter: String?): List<AqiEntity?>?

    @Query("SELECT * FROM aqi WHERE id = :id")
    fun getDataById(id: String?): AqiEntity?

    @get:Query("SELECT * FROM aqi ORDER BY autoid DESC LIMIT 1")
    val last: List<AqiEntity?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg aqiEntities: AqiEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aqiEntity: AqiEntity?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun edit(aqiEntity: AqiEntity?)

    @Delete
    fun delete(aqiEntity: AqiEntity?)

    @Query("DELETE FROM aqi WHERE parameter = :parameter")
    fun deleteAll(parameter: String?)

    @Query("DELETE FROM aqi WHERE cityName = :cityName")
    fun deleteByLocation(cityName: String?)
}