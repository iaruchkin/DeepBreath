package com.iaruchkin.deepbreath.ui.intro;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Storage{

    private static final String SHARED_PREF = "INTRO_SHARED_PREF";
    private static final String SHARED_PREF_KEY_COUNTER = "SHOW_INTRO";
    private static boolean counter = false;

    private void saveCounter(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(SHARED_PREF_KEY_COUNTER, counter);
        editor.apply();
    }
    private void initCounter(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        counter = sharedPref.getBoolean(SHARED_PREF_KEY_COUNTER, false);
    }

    public static boolean needToShowIntro(Context context){
        Storage storage = new Storage();
        storage.initCounter(context);
        counter = !counter;
        storage.saveCounter(context);

        return counter;
    }
}