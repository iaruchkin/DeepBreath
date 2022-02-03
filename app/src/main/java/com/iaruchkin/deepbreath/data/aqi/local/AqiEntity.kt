package com.iaruchkin.deepbreath.data.aqi.local

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.iaruchkin.deepbreath.room.utils.DateConverter

@Entity(tableName = "aqi")
@TypeConverters(DateConverter::class)
class AqiEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @ColumnInfo(name = "autoid")
    var autoid: Long = 0

    @ColumnInfo(name = "idx")
    var idx: String? = null

    @ColumnInfo(name = "parameter")
    var parameter: String = ""

    @ColumnInfo(name = "cityName")
    var cityName: String = ""

    @ColumnInfo(name = "cityUrl")
    var cityUrl: String? = null

    @ColumnInfo(name = "countryName")
    var countryName: String? = null

    @ColumnInfo(name = "stateName")
    var stateName: String? = null

    @ColumnInfo(name = "aqi")
    var aqi: Int = 0

    @ColumnInfo(name = "date")
    var date: String = ""

    @ColumnInfo(name = "date_epoch")
    var dateEpoch: Int = 0

    @ColumnInfo(name = "locationLat")
    var locationLat = 0.0

    @ColumnInfo(name = "locationLon")
    var locationLon = 0.0

    @ColumnInfo(name = "Co")
    var co: Double? = null

    @ColumnInfo(name = "No2")
    var no2: Double? = null

    @ColumnInfo(name = "O3")
    var o3: Double? = null

    @ColumnInfo(name = "pm10")
    var pm10: Int? = null

    @ColumnInfo(name = "pm25")
    var pm25: Int? = null

    @ColumnInfo(name = "So2")
    var so2: Double? = null

    @ColumnInfo(name = "h")
    var h: Double? = null

    @ColumnInfo(name = "p")
    var p: Double? = null

    @ColumnInfo(name = "w")
    var w: Double? = null

    @ColumnInfo(name = "wg")
    var wg: Double? = null


    fun getCoordinates(): Location {
        return Location("AqiEntity Location").apply {
            latitude = locationLat
            longitude = locationLon
        }
    }

    override fun toString(): String {
        return "AqiEntity{" +
                "id='" + id + '\'' +
                "parameter='" + parameter + '\'' +
                ", mAqi= " + aqi + '\'' +
                ", mCityName='" + cityName + '\'' +
                ", mDate='" + date + '\'' +
                '}'
    }
}