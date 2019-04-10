package com.iaruchkin.deepbreath.utils;

public final class ConditionUtils {
    /**this helps parse offline condition.json file
     *
     * @param code
     * @return
     */
    public static int getConditionCode(Integer code){

        switch (code){
            case 1000:
                return 0;
            case 1003:
                return 1;
            case 1006:
                return 2;
            case 1009:
                return 3;
            case 1030:
                return 4;
            case 1063:
                return 5;
            case 1066:
                return 6;
            case 1069:
                return 7;
            case 1072:
                return 8;
            case 1087:
                return 9;
            case 1114:
                return 10;
            case 1117:
                return 11;
            case 1135:
                return 12;
            case 1147:
                return 13;
            case 1150:
                return 14;
            case 1153:
                return 15;
            case 1168:
                return 16;
            case 1171:
                return 17;
            case 1180:
                return 18;
            case 1183:
                return 19;
            case 1186:
                return 20;
            case 1189:
                return 21;
            case 1192:
                return 22;
            case 1195:
                return 23;
            case 1198:
                return 24;
            case 1201:
                return 25;
            case 1204:
                return 26;
            case 1207:
                return 27;
            case 1210:
                return 28;
            case 1213:
                return 29;
            case 1216:
                return 30;
            case 1219:
                return 31;
            case 1222:
                return 32;
            case 1225:
                return 33;
            case 1237:
                return 34;
            case 1240:
                return 35;
            case 1243:
                return 36;
            case 1246:
                return 37;
            case 1249:
                return 38;
            case 1252:
                return 39;
            case 1255:
                return 40;
            case 1258:
                return 41;
            case 1261:
                return 42;
            case 1264:
                return 43;
            case 1273:
                return 44;
            case 1276:
                return 45;
            case 1279:
                return 46;
            case 1282:
                return 47;

            default:
                return 0;
        }
    }

    private ConditionUtils() {
        throw new AssertionError("No instances");
    }
}
