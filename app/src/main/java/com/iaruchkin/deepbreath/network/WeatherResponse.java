
package com.iaruchkin.deepbreath.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Current;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Forecast;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Forecastday;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Location;

import java.util.List;

public class WeatherResponse {

//    @SerializedName("status")
//    @Expose
//    private String status;
//    @SerializedName("data")
//    @Expose
//    private Data data;
//
//    @SerializedName("results")
//    private List<Forecastday> forecastDay;
//
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Data getData() {
//        return data;
//    }
//
//    public void setData(Data data) {
//        this.data = data;
//    }
//
//    public List<Forecastday> getWeather() {
//        return forecastDay;
//    }

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

}


