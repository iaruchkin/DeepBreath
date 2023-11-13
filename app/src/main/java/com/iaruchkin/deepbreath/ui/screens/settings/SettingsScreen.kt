package com.iaruchkin.deepbreath.ui.screens.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    Text(text = "TODO Settings screen")

}

@Preview(name = "Settings", showBackground = true)
@Composable
fun Preview() {

}