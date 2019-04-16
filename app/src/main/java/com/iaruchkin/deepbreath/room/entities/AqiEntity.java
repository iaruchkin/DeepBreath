package com.iaruchkin.deepbreath.room.entities;

import com.iaruchkin.deepbreath.room.utils.DateConverter;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "aqi")
@TypeConverters(DateConverter.class)

public class AqiEntity {

    public AqiEntity() {
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "idx")
    private String mIdx;

    @NonNull
    @ColumnInfo(name = "parameter")
    private String parameter;

    @NonNull
    @ColumnInfo(name ="locationLat")
    private double locationLat;

    @NonNull
    @ColumnInfo(name ="locationLon")
    private double locationLon;

    @NonNull
    @ColumnInfo(name = "cityName")
    private String mCityName;

    @ColumnInfo(name = "cityUrl")
    private String mCityUrl;

    @ColumnInfo(name = "countryName")
    private String mCountryName;

    @ColumnInfo(name = "stateName")
    private String mStateName;

    @NonNull
    @ColumnInfo(name = "aqi")
    private Integer mAqi;

    @ColumnInfo(name = "Co")
    private Double mCo;

    @ColumnInfo(name = "h")
    private Double mH;

    @ColumnInfo(name = "No2")
    private Double mNo2;

    @ColumnInfo(name = "O3")
    private Double mO3;

    @ColumnInfo(name = "p")
    private Double mP;

    @ColumnInfo(name = "pm10")
    private Integer mPm10;

    @ColumnInfo(name = "pm25")
    private Integer mPm25;

    @ColumnInfo(name = "So2")
    private Double mSo2;

    @ColumnInfo(name = "w")
    private Double mW;

    @ColumnInfo(name = "wg")
    private Double mWg;

    @NonNull
    @ColumnInfo(name = "date")
    private String mDate;

    @ColumnInfo(name = "date_epoch")
    private Integer mDateEpoch;

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getIdx() {
        return mIdx;
    }

    @NonNull
    public String getParameter() {
        return parameter;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public double getLocationLon() {
        return locationLon;
    }

    @NonNull
    public String getCityName() {
        return mCityName;
    }

    @NonNull
    public String getCityUrl() {
        return mCityUrl;
    }

    @NonNull
    public String getCountryName() {
        return mCountryName;
    }

    @NonNull
    public String getStateName() {
        return mStateName;
    }

    @NonNull
    public Integer getAqi() {
        return mAqi;
    }

    public Double getCo() {
        return mCo;
    }

    public Double getH() {
        return mH;
    }

    public Double getNo2() {
        return mNo2;
    }

    public Double getO3() {
        return mO3;
    }

    public Double getP() {
        return mP;
    }

    public Integer getPm10() {
        return mPm10;
    }

    public Integer getPm25() {
        return mPm25;
    }

    public Double getSo2() {
        return mSo2;
    }

    public Double getW() {
        return mW;
    }

    public Double getWg() {
        return mWg;
    }

    @NonNull
    public String getDate() {
        return mDate;
    }

    @NonNull
    public Integer getDateEpoch() {
        return mDateEpoch;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setIdx(@NonNull String idx) {
        mIdx = idx;
    }

    public void setParameter(@NonNull String parameter) {
        this.parameter = parameter;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }

    public void setCityName(@NonNull String cityName) {
        mCityName = cityName;
    }

    public void setCityUrl(@NonNull String cityUrl) {
        mCityUrl = cityUrl;
    }

    public void setCountryName(@NonNull String mCountryName) {
        this.mCountryName = mCountryName;
    }

    public void setStateName(@NonNull String mStateName) {
        this.mStateName = mStateName;
    }

    public void setAqi(@NonNull Integer aqi) {
        mAqi = aqi;
    }

    public void setCo(@NonNull Double co) {
        mCo = co;
    }

    public void setH(@NonNull Double h) {
        mH = h;
    }

    public void setNo2(@NonNull Double no2) {
        mNo2 = no2;
    }

    public void setO3(@NonNull Double o3) {
        mO3 = o3;
    }

    public void setP(@NonNull Double p) {
        mP = p;
    }

    public void setPm10(@NonNull Integer pm10) {
        mPm10 = pm10;
    }

    public void setPm25(@NonNull Integer pm25) {
        mPm25 = pm25;
    }

    public void setSo2(@NonNull Double so2) {
        mSo2 = so2;
    }

    public void setW(@NonNull Double w) {
        mW = w;
    }

    public void setWg(@NonNull Double wg) {
        mWg = wg;
    }

    public void setDate(@NonNull String date) {
        mDate = date;
    }

    public void setDateEpoch(@NonNull Integer mDateEpoch) {
        this.mDateEpoch = mDateEpoch;
    }

    @Override
    public String toString() {
        return "AqiEntity{" +
                "id='" + id + '\'' +
                "parameter='" + parameter + '\'' +
                ", mAqi= "+ mAqi + '\'' +
                ", mCityName='" + mCityName + '\'' +
                ", mDate='" + mDate + '\'' +
                '}';
    }
}