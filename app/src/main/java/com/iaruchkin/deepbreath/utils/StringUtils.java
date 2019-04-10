package com.iaruchkin.deepbreath.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;

public final class StringUtils {

    public static String formatDate(@NonNull long epoch, String DATE_FORMAT){
        Date date = new Date(epoch*1000);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }

    public static String formatDateAqi(@NonNull long epoch, String DATE_FORMAT){
        Date date = new Date((epoch*1000 - TimeZone.getDefault().getRawOffset()));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        return sdf.format(date);
    }

    public static String formatTime(@NonNull String dateStr, String DATE_FORMAT){

        SimpleDateFormat date12Format = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        SimpleDateFormat date24Format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        try {
            return date24Format.format(date12Format.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String transliterateLatToRus(String message, String country){
        if(Locale.getDefault().getLanguage().equals("ru") && country.equals("Russia")) {
            String[] abcLat = {"sch", "ts", "ch", "sh", "zh", "ju", "ja", "ya", " ", "a", "b", "v", "g", "d", "e", "e", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "i", "e", "Sch", "Ts", "Ch", "Sh", "Zh", "Ju", "Ja", "A", "B", "V", "G", "D", "E", "E", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "", "I", "", "E", "", "", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
            char[] abcCyr = {'щ', 'ц', 'ч', 'ш', 'з', 'ю', 'я', 'я', ' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ы', 'э', 'Щ', 'Ц', 'Ч', 'Ш', 'Ж', 'Ю', 'Я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ъ', 'Ы', 'Ь', 'Э', 'ъ', 'ь', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

            StringBuilder builder = new StringBuilder();

            for (int x = 0; x < abcLat.length; x++) {
                if (message.startsWith(abcLat[x])) {
                    message = message.replaceFirst(abcLat[x], "");
                    builder.append(abcCyr[x]);
                    x = 0;
                } else if (message.length() == 0) return builder.toString();
            }
        }
        return message;
    }

    public static String transliterateRusToLat(String message){
        char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х', 'ц','ч', 'ш','щ','ъ','ы','ь','э', 'ю','я','А','Б','В','Г','Д','Е','Ё', 'Ж','З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х', 'Ц', 'Ч','Ш', 'Щ','Ъ','Ы','Ь','Э','Ю','Я','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","i","y","k","l","m","n","o","p","r","s","t","u","f","h","ts","ch","sh","sch", "","i", "","e","ju","ja","A","B","V","G","D","E","E","Zh","Z","I","Y","K","L","M","N","O","P","R","S","T","U","F","H","Ts","Ch","Sh","Sch", "","I", "","E","Ju","Ja","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    private StringUtils() {
        throw new AssertionError("No instances");
    }
}
