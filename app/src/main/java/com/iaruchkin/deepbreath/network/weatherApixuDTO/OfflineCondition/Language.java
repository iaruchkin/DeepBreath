
package com.iaruchkin.deepbreath.network.weatherApixuDTO.OfflineCondition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language {

    @SerializedName("lang_name")
    @Expose
    private String langName;
    @SerializedName("lang_iso")
    @Expose
    private String langIso;
    @SerializedName("day_text")
    @Expose
    private String dayText;
    @SerializedName("night_text")
    @Expose
    private String nightText;

    public String getLangName() {
        return langName;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public String getLangIso() {
        return langIso;
    }

    public void setLangIso(String langIso) {
        this.langIso = langIso;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getNightText() {
        return nightText;
    }

    public void setNightText(String nightText) {
        this.nightText = nightText;
    }

}
