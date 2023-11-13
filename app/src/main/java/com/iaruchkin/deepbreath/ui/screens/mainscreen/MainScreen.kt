package com.iaruchkin.deepbreath.ui.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.iaruchkin.deepbreath.navigation.Navigation
import com.iaruchkin.deepbreath.navigation.Screen
import com.iaruchkin.deepbreath.navigation.currentRoute
import com.iaruchkin.deepbreath.navigation.navigationTitle
import com.iaruchkin.deepbreath.ui.components.appbar.AppBarWithArrow
import com.iaruchkin.deepbreath.ui.components.appbar.HomeAppBar


@Composable
fun MainScreen() {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    // internet connection
//    val connection by connectivityState()
//    val isConnected = connection === ConnectionState.Available

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            when (currentRoute(navController)) {
                Screen.Home.route -> {
                    HomeAppBar(
                        title = "",
                        openSearch = { navController.navigate(Screen.Search.route) },
                        openSettings = { navController.navigate(Screen.Settings.route) }
                    )
                }
                else -> {
                    AppBarWithArrow(navigationTitle(navController)) {
                        navController.popBackStack()
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Navigation(navController)
        }
    }

}
