package com.iaruchkin.deepbreath.ui.screens.aqi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iaruchkin.deepbreath.data.DataState
import com.iaruchkin.deepbreath.data.model.AqiCn
import com.iaruchkin.deepbreath.navigation.Screen
import com.iaruchkin.deepbreath.ui.components.WeatherItem
import com.iaruchkin.deepbreath.ui.theme.DeepBreathTheme


@Composable
fun AqiScreen(navController: NavController?) {
    val aqiViewModel = hiltViewModel<AqiViewModel>()
    val aqiDetail = aqiViewModel.aqiDetailState.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        aqiViewModel.aqiDetailApi()
    }

    var isLoading = true
    var isValid = true
    var data: AqiCn? = null

    when(aqiDetail){
        DataState.Loading -> {
            isLoading = true
        }
        is DataState.Success -> {
            data = aqiDetail.data
            isValid = true
            isLoading = false
        }
        is DataState.Error, null -> {
            isLoading = false
            isValid = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            WeatherItem(
                city = data?.data?.city?.name.toString(),
                date = data?.data?.time?.s.toString(),
                aqiLevel = data?.data?.aqi.toString(),
                aqiDescription = "pre unhealthy",
                isLoading = isLoading,
                isInvalidData = isValid,
            ) {
                navController?.navigate(Screen.Detail.route.plus("/iaqi"))
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    DeepBreathTheme(false) {
        AqiScreen(null)
    }
}