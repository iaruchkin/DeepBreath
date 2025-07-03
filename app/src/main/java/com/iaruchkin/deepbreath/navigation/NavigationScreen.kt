package com.iaruchkin.deepbreath.navigation

sealed class Screen(
    val route: String,
    val objectName: String = "",
    val objectPath: String = ""
    ) {
    data object Welcome : Screen("welcome_screen")
    data object Home : Screen("home_screen")
    data object Detail : Screen("aqi_detail_screen", objectName = "item", objectPath = "/{item}")
    data object Settings : Screen("settings_screen")
    data object Search : Screen("search_screen")
}