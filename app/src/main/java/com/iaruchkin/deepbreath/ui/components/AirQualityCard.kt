package com.iaruchkin.deepbreath.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.data.model.AqiLevel

@Composable
fun AirQualityCard(
    aqiValue: Int?,
    isLoading: Boolean,
    isInvalidData: Boolean,
    onCardClick: () -> Unit
) {

    val aqiLevel = AqiLevel.getPollutionLevel(aqiValue)

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .defaultMinSize(100.dp, 126.dp)
            .clickable(onClick = onCardClick),
        elevation = 8.dp,
        backgroundColor = colorResource(id = aqiLevel.color)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier
                    .padding(20.dp)
            )
        } else Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isInvalidData) {
                Text(
                    text = "Invalid AqiData",
                    color = Color.Gray,
                )
            }

            Text(
                text = "aqi",
                color = Color.Gray,
                fontSize = 18.sp,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Text(
                text = aqiValue?.toString() ?: stringResource(id = R.string.ellipses),
                color = Color.Black,
                fontSize = 50.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Text(
                text = stringResource(id = aqiLevel.pollutionLevel),
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AirQualityCardPreview() {
    AirQualityCard(
        aqiValue = 30,
        isLoading = false,
        isInvalidData = false
    ) {}
}

@Preview(showBackground = true)
@Composable
fun AirQualityCardLoadingPreview() {
    AirQualityCard(
        aqiValue = 100,
        isLoading = true,
        isInvalidData = false
    ) {}
}
