package com.iaruchkin.deepbreath.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.ui.screens.aqi.AqiScreen
import com.iaruchkin.deepbreath.ui.screens.detail.DetailScreen
import com.iaruchkin.deepbreath.ui.screens.entry.WelcomeScreen
import com.iaruchkin.deepbreath.ui.screens.search.SearchScreen
import com.iaruchkin.deepbreath.ui.screens.settings.SettingsScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
    val firstOpen = true //todo create preferences service
    NavHost(
        navController,
        startDestination = if (firstOpen) Screen.Welcome.route else Screen.Home.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen()
        }
        composable(Screen.Home.route) {
            AqiScreen {
                navController.navigate(Screen.Detail.route.plus("/iaqi"))
            }
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController)
        }
        composable(
            Screen.Detail.route.plus(Screen.Detail.objectPath),
//            arguments = listOf(navArgument(Screen.Detail.objectName) {
//                type = NavType.IntType
//            })
        ) {
            label = stringResource(R.string.title_activity_detail)
//            val movieId = it.arguments?.getInt(Screen.Detail.objectName)
//            movieId?.let {
            DetailScreen(
                navController = navController, 0
            )
//            }
        }
    }
}

@Composable
fun navigationTitle(navController: NavController): String {
    return when (currentRoute(navController)) {
        Screen.Detail.route -> stringResource(id = R.string.title_activity_detail)
        Screen.Settings.route -> stringResource(id = R.string.title_activity_settings)
        Screen.Search.route -> stringResource(id = R.string.station_search)
        else -> {
            ""
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}
