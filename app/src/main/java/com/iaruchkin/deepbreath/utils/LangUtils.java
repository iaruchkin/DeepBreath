package com.iaruchkin.deepbreath.utils;

import java.util.Locale;

public final class LangUtils {

    /**this helps parse offline condition.json file
     *
     * @return
     */
    public static Integer getLangCode(){

        String deviceLocale= Locale.getDefault().getLanguage();

        switch (deviceLocale){
            case "ar":
                return 0;
            case "bn":
                return 1;
            case "bg":
                return 2;
            case "zh":
                return 3;
            case "zh_tw":
                return 4;
            case "cs":
                return 5;
            case "nl":
                return 6;
            case "fi":
                return 7;
            case "fr":
                return 8;
            case "de":
                return 9;
            case "el":
                return 10;
            case "hi":
                return 11;
            case "hu":
                return 12;
            case "it":
                return 13;
            case "ja":
                return 14;
            case "jv":
                return 15;
            case "ko":
                return 16;
            case "zh_cmn":
                return 17;
            case "mr":
                return 18;
            case "pl":
                return 19;
            case "pt":
                return 20;
            case "pa":
                return 21;
            case "ro":
                return 22;
            case "ru":
                return 23;
            case "sr":
                return 24;
            case "si":
                return 25;
            case "sk":
                return 26;
            case "es":
                return 27;
            case "sv":
                return 28;
            case "ta":
                return 29;
            case "te":
                return 30;
            case "tr":
                return 31;
            case "uk":
                return 32;
            case "ur":
                return 33;
            case "vi":
                return 34;
         case "zh_wuu":
                return 35;
        case "zh_hsn":
                return 36;
        case "zh_yue":
                return 37;
            case "zu":
                return 38;

            default:
                return 39;
        }
    }

    private LangUtils() {
        throw new AssertionError("No instances");
    }
}
