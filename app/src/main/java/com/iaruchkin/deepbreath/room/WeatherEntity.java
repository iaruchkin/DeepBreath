package com.iaruchkin.deepbreath.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "aqi")
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
    @ColumnInfo(name = "aqi")
    private String mAqi;

    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getLocation() {
        return mLocation;
    }

    @NonNull
    public String getAqi() {
        return mAqi;
    }

    @NonNull
    public String getDate() {
        return mDate;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public void setLocation(@NonNull String section) {
        mLocation = section;
    }

    public void setAqi(@NonNull String aqi) {
        mAqi = aqi;
    }

    public void setDate(@NonNull String date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "mId='" + mId + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mDate='" + mDate + '\'' +
                '}';
    }
}