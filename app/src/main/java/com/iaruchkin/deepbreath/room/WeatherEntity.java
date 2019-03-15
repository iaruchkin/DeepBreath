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
    private String id;

    @NonNull
    @ColumnInfo(name = "location")
    private String mLocation;

    @NonNull
    @ColumnInfo(name ="last_updated_epoch")
    private int last_updated_epoch;

    @NonNull
    @ColumnInfo(name ="wind_degree")
    private int wind_degree;

    @NonNull
    @ColumnInfo(name ="humidity")
    private int humidity;

    @NonNull
    @ColumnInfo(name ="cloud")
    private int cloud;

    @NonNull
    @ColumnInfo(name ="last_updated")
    private String last_updated;

    @NonNull
    @ColumnInfo(name ="wind_dir")
    private String wind_dir;

    @NonNull
    @ColumnInfo(name ="temp_c")
    private double temp_c;

    @NonNull
    @ColumnInfo(name ="temp_f")
    private double temp_f;

    @NonNull
    @ColumnInfo(name ="wind_mph")
    private double wind_mph;

    @NonNull
    @ColumnInfo(name ="wind_kph")
    private double wind_kph;

    @NonNull
    @ColumnInfo(name ="pressure_mb")
    private double pressure_mb;

    @NonNull
    @ColumnInfo(name ="pressure_in")
    private double pressure_in;

    @NonNull
    @ColumnInfo(name ="precip_mm")
    private double precip_mm;

    @NonNull
    @ColumnInfo(name ="precip_in")
    private double precip_in;

    @NonNull
    @ColumnInfo(name ="feelslike_c")
    private double feelslike_c;

    @NonNull
    @ColumnInfo(name ="feelslike_f")
    private double feelslike_f;

//    @NonNull
    @ColumnInfo(name ="conditionText")
    private String conditionText;

//    @NonNull
//    @ColumnInfo(name ="conditionIcon")
//    private String conditionIcon;

    @NonNull
    @ColumnInfo(name ="conditionCode")
    private int conditionCode;

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getLocation() {
        return mLocation;
    }

    public int getLast_updated_epoch() {
        return last_updated_epoch;
    }

    public int getWind_degree() {
        return wind_degree;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getCloud() {
        return cloud;
    }

    @NonNull
    public String getLast_updated() {
        return last_updated;
    }

    @NonNull
    public String getWind_dir() {
        return wind_dir;
    }

    public double getTemp_c() {
        return temp_c;
    }

    public double getTemp_f() {
        return temp_f;
    }

    public double getWind_mph() {
        return wind_mph;
    }

    public double getWind_kph() {
        return wind_kph;
    }

    public double getPressure_mb() {
        return pressure_mb;
    }

    public double getPressure_in() {
        return pressure_in;
    }

    public double getPrecip_mm() {
        return precip_mm;
    }

    public double getPrecip_in() {
        return precip_in;
    }

    public double getFeelslike_c() {
        return feelslike_c;
    }

    public double getFeelslike_f() {
        return feelslike_f;
    }

    @NonNull
    public String getConditionText() {
        return conditionText;
    }

    public int getConditionCode() {
        return conditionCode;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setLocation(@NonNull String mLocation) {
        this.mLocation = mLocation;
    }

    public void setLast_updated_epoch(int last_updated_epoch) {
        this.last_updated_epoch = last_updated_epoch;
    }

    public void setWind_degree(int wind_degree) {
        this.wind_degree = wind_degree;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public void setLast_updated(@NonNull String last_updated) {
        this.last_updated = last_updated;
    }

    public void setWind_dir(@NonNull String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public void setTemp_c(double temp_c) {
        this.temp_c = temp_c;
    }

    public void setTemp_f(double temp_f) {
        this.temp_f = temp_f;
    }

    public void setWind_mph(double wind_mph) {
        this.wind_mph = wind_mph;
    }

    public void setWind_kph(double wind_kph) {
        this.wind_kph = wind_kph;
    }

    public void setPressure_mb(double pressure_mb) {
        this.pressure_mb = pressure_mb;
    }

    public void setPressure_in(double pressure_in) {
        this.pressure_in = pressure_in;
    }

    public void setPrecip_mm(double precip_mm) {
        this.precip_mm = precip_mm;
    }

    public void setPrecip_in(double precip_in) {
        this.precip_in = precip_in;
    }

    public void setFeelslike_c(double feelslike_c) {
        this.feelslike_c = feelslike_c;
    }

    public void setFeelslike_f(double feelslike_f) {
        this.feelslike_f = feelslike_f;
    }

    public void setConditionText(@NonNull String conditionText) {
        this.conditionText = conditionText;
    }

    public void setConditionCode(int conditionCode) {
        this.conditionCode = conditionCode;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "id='" + id + '\'' +
                ", location=" + mLocation +
                ", last_updated_epoch=" + last_updated_epoch +
                ", wind_degree=" + wind_degree +
                ", humidity=" + humidity +
                ", cloud=" + cloud +
                ", last_updated='" + last_updated + '\'' +
                ", wind_dir='" + wind_dir + '\'' +
                ", temp_c=" + temp_c +
                ", temp_f=" + temp_f +
                ", wind_mph=" + wind_mph +
                ", wind_kph=" + wind_kph +
                ", pressure_mb=" + pressure_mb +
                ", pressure_in=" + pressure_in +
                ", precip_mm=" + precip_mm +
                ", precip_in=" + precip_in +
                ", feelslike_c=" + feelslike_c +
                ", feelslike_f=" + feelslike_f +
                ", conditionText='" + conditionText + '\'' +
                ", conditionCode=" + conditionCode +
                '}';
    }
}