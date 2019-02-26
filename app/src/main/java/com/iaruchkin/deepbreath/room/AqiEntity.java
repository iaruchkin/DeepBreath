package com.iaruchkin.deepbreath.room;

import com.iaruchkin.deepbreath.network.aqicnDTO.Iaqi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "aqi")
public class AqiEntity {

    public AqiEntity() {
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
    @ColumnInfo(name = "pm10")
    private Integer mPm10;

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

    @NonNull
    public Integer getPm10() {
        return mPm10;
    }

    public void setId(@NonNull String id) {
        mId = id;
    }

    public void setLocation(@NonNull String location) {
        mLocation = location;
    }

    public void setAqi(@NonNull String aqi) {
        mAqi = aqi;
    }

    public void setDate(@NonNull String date) {
        mDate = date;
    }

    public void setPm10(@NonNull Integer pm10) {
        mPm10 = pm10;
    }

    @Override
    public String toString() {
        return "AqiEntity{" +
                "mId='" + mId + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mDate='" + mDate + '\'' +
                '}';
    }
}