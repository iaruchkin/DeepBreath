package com.iaruchkin.deepbreath.room.daos

import androidx.room.*
import com.iaruchkin.deepbreath.room.entities.FavoritesEntity

@Dao
interface FavoritesDao {
    @get:Query("SELECT * FROM favorites")
    val all: List<FavoritesEntity?>?

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getFavoriteById(id: String): FavoritesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg filtersEntities: FavoritesEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filtersEntity: FavoritesEntity?)

    @Delete
    fun deleteAll(filtersEntity: FavoritesEntity?)

    @Query("DELETE FROM favorites WHERE id = :id")
    fun deleteById(id: String)
}