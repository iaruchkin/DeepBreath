package com.iaruchkin.deepbreath.utils

class ConditionUtils private constructor() {

    init {
        throw AssertionError("No instances")
    }

    companion object {
        /**this helps parse offline condition.json file
         *
         * @param code
         * @return
         */
        fun getConditionCode(code: Int): Int {
//todo попроавить описание
            when (code) {
                800 -> return 0     //sunny-clear
                801 -> return 1     //Partly Cloudy
                802, 803 -> return 2    //Cloudy
                804 -> return 3    //Overcast
                701 -> return 4    //Mist
//                1063 -> return 5    //Patchy rain nearby
//                1066 -> return 6    //Patchy snow nearby
//                1069 -> return 7    //Patchy sleet nearby
//                1072 -> return 8    //Patchy freezing drizzle nearby
                210, 211 -> return 9    //Thundery outbreaks in nearby
                1114 -> return 10   //Blowing snow
                1117 -> return 11   //Blizzard
                741 -> return 12   //Fog
//                1147 -> return 13   //Freezing fog
//                300 -> return 14   //Patchy light drizzle
                300, 301 -> return 15   //Light drizzle
                302, 310, 311 -> return 16   //Freezing drizzle
                312, 313, 314 -> return 17   //Heavy freezing drizzle
//                1180 -> return 18   //Patchy light rain
                500 -> return 19   //Light rain
//                501 -> return 20   //Moderate rain at times
                501 -> return 21   //Moderate rain
                502 -> return 22   //Heavy rain at times
                503, 504 -> return 23   //Heavy rain
//                1198 -> return 24   //Light freezing rain
                511 -> return 25   //Moderate or heavy freezing rain
//                1204 -> return 26   //Light sleet
                611 -> return 27   //Moderate or heavy sleet
//                1210 -> return 28   //Patchy light snow
                600 -> return 29   //Light snow
//                601 -> return 30   //Patchy moderate snow
                601 -> return 31   //Moderate snow
//                602 -> return 32   //Patchy heavy snow
                602 -> return 33   //Heavy snow
//                1237 -> return 34   //Ice pellets
                520 -> return 35   //Light rain shower
                521, 531 -> return 36   //Moderate or heavy rain shower
                522 -> return 37   //Torrential rain shower
                612 -> return 38   //Light sleet showers
                613, 615, 616 -> return 39   //Moderate or heavy sleet showers
                620 -> return 40   //Light snow showers
                621, 622 -> return 41   //Moderate or heavy snow showers
//                1261 -> return 42   //Light showers of ice pellets
//                1264 -> return 43   //Moderate or heavy showers of ice pellets
                200, 201 -> return 44   //Patchy light rain in area with thunder
                202, 212, 221, 230, 231, 232 -> return 45   //Moderate or heavy rain in area with thunder
//                1279 -> return 46   //Patchy light snow in area with thunder
//                1282 -> return 47   //Moderate or heavy snow in area with thunder

                else -> return 2
            }
        }
    }
}
