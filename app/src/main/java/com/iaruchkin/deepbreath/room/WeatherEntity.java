package com.iaruchkin.deepbreath.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather")
public class WeatherEntity {

    public WeatherEntity() {
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String mId;

    @NonNull
    @ColumnInfo(name = "location")
    private String mLocation;

    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    @NonNull
    @ColumnInfo(name = "temperature")
    private Double mTemperature;

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getLocation() {
        return mLocation;
    }

    @NonNull
    public String getDate() {
        return mDate;
    }

    @NonNull
    public Double getTemperature() {
        return mTemperature;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public void setLocation(@NonNull String location) {
        mLocation = location;
    }

    public void setDate(@NonNull String date) {
        mDate = date;
    }

    public void setTemperature(@NonNull Double temperature) {
        mTemperature = temperature;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "mId='" + mId + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mTemperature='" + mTemperature + '\'' +
                '}';
    }
}