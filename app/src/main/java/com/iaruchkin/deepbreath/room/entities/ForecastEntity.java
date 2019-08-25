package com.iaruchkin.deepbreath.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.iaruchkin.deepbreath.room.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "forecast")
@TypeConverters(DateConverter.class)

public class ForecastEntity {

    public ForecastEntity() {
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "autoid")
    private long autoid;

    public long getAutoid() {
        return autoid;
    }

    public void setAutoid(long autoid) {
        this.autoid = autoid;
    }

    @NonNull
    @ColumnInfo(name = "parameter")
    private String parameter;

    @NonNull
    @ColumnInfo(name ="date")
    private Date date;

    @NonNull
    @ColumnInfo(name ="date_epoch")
    private int date_epoch;

    @NonNull
    @ColumnInfo(name ="isDay")
    private int isDay;

    @NonNull
    @ColumnInfo(name ="maxtemp_c")
    private double maxtemp_c;

    @NonNull
    @ColumnInfo(name ="maxtemp_f")
    private double maxtemp_f;

    @NonNull
    @ColumnInfo(name ="mintemp_c")
    private double mintemp_c;

    @NonNull
    @ColumnInfo(name ="mintemp_f")
    private double mintemp_f;

    @NonNull
    @ColumnInfo(name ="avgtemp_c")
    private double avgtemp_c;

    @NonNull
    @ColumnInfo(name ="avgtemp_f")
    private double avgtemp_f;

    @NonNull
    @ColumnInfo(name ="maxwind_mph")
    private double maxwind_mph;

    @NonNull
    @ColumnInfo(name ="maxwind_kph")
    private double maxwind_kph;

    @NonNull
    @ColumnInfo(name ="totalprecip_mm")
    private double totalprecip_mm;

    @NonNull
    @ColumnInfo(name ="totalprecip_in")
    private double totalprecip_in;

    @NonNull
    @ColumnInfo(name ="conditionText")
    private String conditionText;

//    @NonNull
//    @ColumnInfo(name ="conditionIcon")
//    private String conditionIcon;

    @NonNull
    @ColumnInfo(name ="conditionCode")
    private int conditionCode;

//location

    @NonNull
    @ColumnInfo(name ="locationName")
    private String locationName;

    @NonNull
    @ColumnInfo(name ="locationRegion")
    private String locationRegion;

    @NonNull
    @ColumnInfo(name ="locationCountry")
    private String locationCountry;

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

//astro

    @NonNull
    @ColumnInfo(name ="sunrise")
    private String sunrise;

    @NonNull
    @ColumnInfo(name ="sunset")
    private String sunset;

    @NonNull
    @ColumnInfo(name ="moonrise")
    private String moonrise;

    @NonNull
    @ColumnInfo(name ="moonset")
    private String moonset;

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getParameter() {
        return parameter;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public int getDate_epoch() {
        return date_epoch;
    }

    public int getIsDay() {
        return isDay;
    }

    public double getMaxtemp_c() {
        return maxtemp_c;
    }

    public double getMaxtemp_f() {
        return maxtemp_f;
    }

    public double getMintemp_c() {
        return mintemp_c;
    }

    public double getMintemp_f() {
        return mintemp_f;
    }

    public double getAvgtemp_c() {
        return avgtemp_c;
    }

    public double getAvgtemp_f() {
        return avgtemp_f;
    }

    public double getMaxwind_mph() {
        return maxwind_mph;
    }

    public double getMaxwind_kph() {
        return maxwind_kph;
    }

    public double getTotalprecip_mm() {
        return totalprecip_mm;
    }

    public double getTotalprecip_in() {
        return totalprecip_in;
    }

//    @NonNull
    public String getConditionText() {
        return conditionText;
    }

    public int getConditionCode() {
        return conditionCode;
    }

    @NonNull
    public String getLocationName() {
        return locationName;
    }

    @NonNull
    public String getLocationRegion() {
        return locationRegion;
    }

    @NonNull
    public String getLocationCountry() {
        return locationCountry;
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

    @NonNull
    public String getSunrise() {
        return sunrise;
    }

    @NonNull
    public String getSunset() {
        return sunset;
    }

    @NonNull
    public String getMoonrise() {
        return moonrise;
    }

    @NonNull
    public String getMoonset() {
        return moonset;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setParameter(@NonNull String mGeo) {
        this.parameter = mGeo;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public void setDate_epoch(int date_epoch) {
        this.date_epoch = date_epoch;
    }

    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }

    public void setMaxtemp_c(double maxtemp_c) {
        this.maxtemp_c = maxtemp_c;
    }

    public void setMaxtemp_f(double maxtemp_f) {
        this.maxtemp_f = maxtemp_f;
    }

    public void setMintemp_c(double mintemp_c) {
        this.mintemp_c = mintemp_c;
    }

    public void setMintemp_f(double mintemp_f) {
        this.mintemp_f = mintemp_f;
    }

    public void setAvgtemp_c(double avgtemp_c) {
        this.avgtemp_c = avgtemp_c;
    }

    public void setAvgtemp_f(double avgtemp_f) {
        this.avgtemp_f = avgtemp_f;
    }

    public void setMaxwind_mph(double maxwind_mph) {
        this.maxwind_mph = maxwind_mph;
    }

    public void setMaxwind_kph(double maxwind_kph) {
        this.maxwind_kph = maxwind_kph;
    }

    public void setTotalprecip_mm(double totalprecip_mm) {
        this.totalprecip_mm = totalprecip_mm;
    }

    public void setTotalprecip_in(double totalprecip_in) {
        this.totalprecip_in = totalprecip_in;
    }

    public void setConditionText(@NonNull String conditionText) {
        this.conditionText = conditionText;
    }

    public void setConditionCode(int conditionCode) {
        this.conditionCode = conditionCode;
    }

    public void setLocationName(@NonNull String locationName) {
        this.locationName = locationName;
    }

    public void setLocationRegion(@NonNull String locationRegion) {
        this.locationRegion = locationRegion;
    }

    public void setLocationCountry(@NonNull String locationCountry) {
        this.locationCountry = locationCountry;
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

    public void setSunrise(@NonNull String sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(@NonNull String sunset) {
        this.sunset = sunset;
    }

    public void setMoonrise(@NonNull String moonrise) {
        this.moonrise = moonrise;
    }

    public void setMoonset(@NonNull String moonset) {
        this.moonset = moonset;
    }

    @Override
    public String toString() {
        return "ForecastEntity{" +
                "id='" + id + '\'' +
                "parameter='" + parameter + '\'' +
                ", date='" + date + '\'' +
//                ", date_epoch=" + date_epoch +
                ", maxtemp_c=" + maxtemp_c +
//                ", maxtemp_f=" + maxtemp_f +
//                ", mintemp_c=" + mintemp_c +
//                ", mintemp_f=" + mintemp_f +
//                ", avgtemp_c=" + avgtemp_c +
//                ", avgtemp_f=" + avgtemp_f +
//                ", maxwind_mph=" + maxwind_mph +
//                ", maxwind_kph=" + maxwind_kph +
//                ", totalprecip_mm=" + totalprecip_mm +
//                ", totalprecip_in=" + totalprecip_in +
                ", conditionText='" + conditionText + '\'' +
                ", conditionCode=" + conditionCode +
                ", locationName='" + locationName + '\'' +
//                ", locationRegion='" + locationRegion + '\'' +
//                ", locationCountry='" + locationCountry + '\'' +
//                ", locationTz_id='" + locationTz_id + '\'' +
//                ", locationLocaltime='" + locationLocaltime + '\'' +
//                ", locationLat=" + locationLat +
//                ", locationLon=" + locationLon +
//                ", locationLocaltime_epoch=" + locationLocaltime_epoch +
//                ", sunrise='" + sunrise + '\'' +
//                ", sunset='" + sunset + '\'' +
//                ", moonrise='" + moonrise + '\'' +
//                ", moonset='" + moonset + '\'' +
                '}';
    }
}