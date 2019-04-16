package com.iaruchkin.deepbreath.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "condition")
public class ConditionEntity {

    public ConditionEntity() {
    }

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String mId;

    //main
    @NonNull
    @ColumnInfo(name = "code")
    private Integer code;

    @NonNull
    @ColumnInfo(name = "day")
    private String day;

    @NonNull
    @ColumnInfo(name = "night")
    private String night;

    @NonNull
    @ColumnInfo(name = "icon")
    private Integer icon;

    //lang
    //main
    @NonNull
    @ColumnInfo(name = "lang")
    private Integer lang;

    @NonNull
    @ColumnInfo(name = "lang_name")
    private String langName;

    @NonNull
    @ColumnInfo(name = "lang_iso")
    private String langIso;

    @NonNull
    @ColumnInfo(name = "day_text")
    private String dayText;

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public Integer getCode() {
        return code;
    }

    @NonNull
    public String getDay() {
        return day;
    }

    @NonNull
    public String getNight() {
        return night;
    }

    @NonNull
    public Integer getIcon() {
        return icon;
    }

    @NonNull
    public Integer getLang() {
        return lang;
    }

    public String getLangName() {
        return langName;
    }

    public String getLangIso() {
        return langIso;
    }

    public String getDayText() {
        return dayText;
    }

    public String getNightText() {
        return nightText;
    }

    @NonNull
    @ColumnInfo(name = "night_text")
    private String nightText;


    public void setId(@NonNull String mId) {
        this.mId = mId;
    }

    public void setCode(@NonNull Integer code) {
        this.code = code;
    }

    public void setDay(@NonNull String day) {
        this.day = day;
    }

    public void setNight(@NonNull String night) {
        this.night = night;
    }

    public void setIcon(@NonNull Integer icon) {
        this.icon = icon;
    }

    public void setLang(@NonNull Integer lang) {
        this.lang = lang;
    }

    public void setLangName(@NonNull String langName) {
        this.langName = langName;
    }

    public void setLangIso(@NonNull String langIso) {
        this.langIso = langIso;
    }

    public void setDayText(@NonNull String dayText) {
        this.dayText = dayText;
    }

    public void setNightText(@NonNull String nightText) {
        this.nightText = nightText;
    }

    @Override
    public String toString() {
        return "ConditionEntity{" +
                "mId='" + mId + '\'' +
                ", code=" + code +
                ", day='" + day + '\'' +
                ", night='" + night + '\'' +
                ", icon=" + icon +
                ", langName='" + langName + '\'' +
                ", langIso='" + langIso + '\'' +
                ", dayText='" + dayText + '\'' +
                ", nightText='" + nightText + '\'' +
                '}';
    }
}