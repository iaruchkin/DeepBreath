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
import com.iaruchkin.deepbreath.data.DataState
import com.iaruchkin.deepbreath.data.model.AqiCn
import com.iaruchkin.deepbreath.data.model.aqi_cn.AqiData
import com.iaruchkin.deepbreath.data.model.aqi_cn.City
import com.iaruchkin.deepbreath.data.model.aqi_cn.Daily
import com.iaruchkin.deepbreath.data.model.aqi_cn.Debug
import com.iaruchkin.deepbreath.data.model.aqi_cn.Forecast
import com.iaruchkin.deepbreath.data.model.aqi_cn.Iaqi
import com.iaruchkin.deepbreath.data.model.aqi_cn.Time
import com.iaruchkin.deepbreath.data.model.aqi_cn.Value
import com.iaruchkin.deepbreath.data.wrapSuccess
import com.iaruchkin.deepbreath.ui.components.WeatherItem
import com.iaruchkin.deepbreath.ui.theme.DeepBreathTheme


@Composable
fun AqiScreen(
    onItemClick: () -> Unit
) {
    val aqiViewModel = hiltViewModel<AqiViewModel>()
    val aqiDetail = aqiViewModel.aqiDetailState.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        aqiViewModel.aqiDetailApi()
    }

    AqiContent(
        aqiDetail = aqiDetail,
        onItemClick = onItemClick
    )
}

@Composable
private fun AqiContent(
    aqiDetail: DataState<AqiCn>?,
    onItemClick: () -> Unit = {},
) {
    var isLoading = true
    var isValid = true
    var aqiCn: AqiCn? = null

    when (aqiDetail) {
        DataState.Loading -> {
            isLoading = true
        }

        is DataState.Success -> {
            aqiCn = aqiDetail.data
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
                aqiData = aqiCn?.data,
                isLoading = isLoading,
                isInvalidData = isValid,
                onItemClick = onItemClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    DeepBreathTheme(false) {
        AqiContent(
            AqiCn(
                data = AqiData(
                    aqi = 30,
                    attributions = listOf(),
                    city = City(
                        geo = listOf(),
                        location = "",
                        name = "Amsterdam",
                        url = "",
                    ),
                    debug = Debug(""),
                    dominentpol = "",
                    forecast = Forecast(
                        daily = Daily(
                            listOf(),
                            listOf(),
                            listOf(),
                            listOf(),
                        )
                    ),
                    iaqi = Iaqi(
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                        Value(0.0),
                    ),
                    idx = 12,
                    time = Time(
                        "",
                        "",
                        "",
                        0,
                    ),
                ),
                status = ""
            ).wrapSuccess()
        )
    }
}