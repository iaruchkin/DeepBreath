
package com.iaruchkin.deepbreath.network.dtos.aqiAvDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("weather")
    @Expose
    private Weather weather;
    @SerializedName("pollution")
    @Expose
    private Pollution pollution;

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Pollution getPollution() {
        return pollution;
    }

    public void setPollution(Pollution pollution) {
        this.pollution = pollution;
    }

}
