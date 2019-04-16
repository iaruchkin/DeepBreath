package com.iaruchkin.deepbreath.network.parsers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iaruchkin.deepbreath.App;
import com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO.OfflineCondition.WeatherCondition;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;

public class ConditionParser {

    private String LOG = "ConditionParser";

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
                Log.e(LOG, e.getMessage(), e);
            }

        Log.e(LOG,tContents);

        Gson gson = new Gson();

        Type collectionType = new TypeToken<Collection<WeatherCondition>>(){}.getType();
        List<WeatherCondition> enums = gson.fromJson(tContents, collectionType);

        Log.e(LOG, enums.get(0).getDay());
        return enums;
    }

    @NonNull
    public List<WeatherCondition> conditions() {
        return weatherConditions;
    }
}
