
package com.iaruchkin.deepbreath.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Current;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Forecast;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.Location;


public class WeatherResponse {

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


