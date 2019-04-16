
package com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO.OfflineCondition;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherCondition {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("night")
    @Expose
    private String night;
    @SerializedName("icon")
    @Expose
    private Integer icon;
    @SerializedName("languages")
    @Expose
    private List<Language> languages = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

}
