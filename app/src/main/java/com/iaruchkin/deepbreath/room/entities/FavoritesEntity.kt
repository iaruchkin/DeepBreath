package com.iaruchkin.deepbreath.room.entities

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class FavoritesEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @ColumnInfo(name = "locationName")
    var locationName: String = ""

    @ColumnInfo(name = "locationLat")
    var locationLat: Double = 0.0

    @ColumnInfo(name = "locationLon")
    var locationLon: Double = 0.0

    @ColumnInfo(name = "aqi")
    var aqi: Int? = null

    fun getCoordinates(): Location {
        return Location("FavoritesEntity").apply {
            latitude = locationLat
            longitude = locationLon
        }
    }

    constructor(id: String, locationName: String, locationLat: Double, locationLon: Double, aqi: Int) {
        this.id = id
        this.locationName = locationName
        this.locationLat = locationLat
        this.locationLon = locationLon
        this.aqi = aqi
    }
}