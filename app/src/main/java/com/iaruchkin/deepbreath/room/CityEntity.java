package com.iaruchkin.deepbreath.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "cities")
@TypeConverters(DateConverter.class)

public class CityEntity {

    public CityEntity() {
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "parameter")
    private String parameter;

    @NonNull
    @ColumnInfo(name = "location")
    private String mLocation;

    @NonNull
    @ColumnInfo(name = "country")
    private String mCountry;

    @NonNull
    @ColumnInfo(name = "region")
    private String mRegion;

    @NonNull
    @ColumnInfo(name ="locationTz_id")
    private String locationTz_id;

    @NonNull
    @ColumnInfo(name ="localtime")
    private String locationLocaltime;

    @NonNull
    @ColumnInfo(name ="locationLat")
    private double locationLat;

    @NonNull
    @ColumnInfo(name ="locationLon")
    private double locationLon;

    @NonNull
    @ColumnInfo(name ="locationLocaltime_epoch")
    private int locationLocaltime_epoch;

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getParameter() {
        return parameter;
    }

    @NonNull
    public String getLocation() {
        return mLocation;
    }

    @NonNull
    public String getCountry() {
        return mCountry;
    }

    @NonNull
    public String getRegion() {
        return mRegion;
    }
    @NonNull
    public String getLocationTz_id() {
        return locationTz_id;
    }

    @NonNull
    public String getLocationLocaltime() {
        return locationLocaltime;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public int getLocationLocaltime_epoch() {
        return locationLocaltime_epoch;
    }


    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setParameter(@NonNull String mGeo) {
        this.parameter = mGeo;
    }

    public void setLocation(@NonNull String mLocation) {
        this.mLocation = mLocation;
    }

    public void setCountry(@NonNull String mCountry) {
        this.mCountry = mCountry;
    }

    public void setRegion(@NonNull String mRegion) {
        this.mRegion = mRegion;
    }

    public void setLocationTz_id(@NonNull String locationTz_id) {
        this.locationTz_id = locationTz_id;
    }

    public void setLocationLocaltime(@NonNull String locationLocaltime) {
        this.locationLocaltime = locationLocaltime;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }

    public void setLocationLocaltime_epoch(int locationLocaltime_epoch) {
        this.locationLocaltime_epoch = locationLocaltime_epoch;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "id='" + id + '\'' +
                "parameter='" + parameter + '\'' +
                ", location=" + mLocation +
                '}';
    }
}