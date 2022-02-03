package com.iaruchkin.deepbreath.network.parsers;

import androidx.annotation.NonNull;

import com.iaruchkin.deepbreath.network.dtos.weatherApixuDTO.OfflineCondition.WeatherCondition;

import java.util.List;

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

//        weatherConditions = response();
    }

//todo refactor

//    private List<WeatherCondition> response() {
//        Context context = App.INSTANCE.getApplicationContext();
//        String tContents = "";
//
//            try {
//                InputStream stream = context.getAssets().open("conditions.json");
//
//                int size = stream.available();
//                byte[] buffer = new byte[size];
//                stream.read(buffer);
//                stream.close();
//                tContents = new String(buffer);
//            } catch (IOException e) {
//                Log.e(LOG, e.getMessage(), e);
//            }
//
//        Log.v(LOG,tContents);
//
//        Gson gson = new Gson();
//        Type collectionType = new TypeToken<Collection<WeatherCondition>>(){}.getType();
//        List<WeatherCondition> enums = gson.fromJson(tContents, collectionType);
//
//        return enums;
//    }

    @NonNull
    public List<WeatherCondition> conditions() {
        return weatherConditions;
    }
}
