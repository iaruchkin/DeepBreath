package com.iaruchkin.deepbreath.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.weatherApixuDTO.OfflineCondition.WeatherCondition;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

public class ConditionParser {

    private static ConditionParser networkSilngleton;
    private List<WeatherCondition> weatherConditions;

    public static synchronized ConditionParser getInstance(){
        if (networkSilngleton == null){
            networkSilngleton = new ConditionParser();
        }
        return networkSilngleton;
    }

    private ConditionParser(){

        weatherConditions = response();
    }

    private List<WeatherCondition> response() {
        Context context = App.INSTANCE.getApplicationContext();
        String tContents = "";

            try {
                InputStream stream = context.getAssets().open("conditions.json");

                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                tContents = new String(buffer);
            } catch (IOException e) {
                //todo Handle exceptions here
            }

        Log.e("Log 1  ",tContents);

        Gson gson = new Gson();

        Type collectionType = new TypeToken<Collection<WeatherCondition>>(){}.getType();
        List<WeatherCondition> enums = gson.fromJson(tContents, collectionType);

        Log.e("Log 2  ", enums.get(0).getDay());
        return enums;
    }

    @NonNull
    public List<WeatherCondition> conditions() {
        return weatherConditions;
    }
}
