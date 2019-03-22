package com.iaruchkin.deepbreath.utils;

import java.util.Locale;

public final class LangUtils {

    public static Integer getLangCode(){

        String deviceLocale= Locale.getDefault().getLanguage();

        switch (deviceLocale){
            case "ru":
                return 23;

            default:
                return 39;
        }
//todo implement all langs

    }

    private LangUtils() {
        throw new AssertionError("No instances");
    }
}
