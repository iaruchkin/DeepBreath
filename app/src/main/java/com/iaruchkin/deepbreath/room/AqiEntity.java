package com.iaruchkin.deepbreath.room;

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
    private String mId;

    @NonNull
    @ColumnInfo(name = "idx")
    private String mIdx;

    @NonNull
    @ColumnInfo(name = "parameter")
    private String parameter;

    @NonNull
    @ColumnInfo(name = "cityGeo")
    private String mCityGeo;

    @NonNull
    @ColumnInfo(name = "cityName")
    private String mCityName;

    @NonNull
    @ColumnInfo(name = "cityUrl")
    private String mCityUrl;

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

//    @NonNull
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

    @NonNull
    @ColumnInfo(name = "date_epoch")
    private Integer mDateEpoch;

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getIdx() {
        return mIdx;
    }

    @NonNull
    public String getParameter() {
        return parameter;
    }

    @NonNull
    public String getCityGeo() {
        return mCityGeo;
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

    @NonNull
    public Integer getPm10() {
        return mPm10;
    }

    @NonNull
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
        mId = id;
    }

    public void setIdx(@NonNull String idx) {
        mIdx = idx;
    }

    public void setParameter(@NonNull String parameter) {
        this.parameter = parameter;
    }

    public void setCityGeo(@NonNull String cityGeo) {
        mCityGeo = cityGeo;
    }

    public void setCityName(@NonNull String cityName) {
        mCityName = cityName;
    }

    public void setCityUrl(@NonNull String cityUrl) {
        mCityUrl = cityUrl;
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
                "mId='" + mId + '\'' +
                "parameter='" + parameter + '\'' +
                ", mAqi= "+ mAqi + '\'' +
                ", mCityName='" + mCityName + '\'' +
                ", mDate='" + mDate + '\'' +
                '}';
    }
}