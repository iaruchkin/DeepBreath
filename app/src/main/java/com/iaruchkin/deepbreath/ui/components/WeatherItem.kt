package com.iaruchkin.deepbreath.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iaruchkin.deepbreath.R
import com.iaruchkin.deepbreath.data.model.aqi_cn.AqiData
import com.iaruchkin.deepbreath.ui.theme.SecondaryTextColor

@Composable
fun WeatherItem(
    aqiData: AqiData?,
    isLoading: Boolean,
    isInvalidData: Boolean,
    onItemClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_location_city_black_24dp),
                tint = SecondaryTextColor,
                contentDescription = null
            )
            Text(
                text = aqiData?.city?.name ?: stringResource(id = R.string.ellipses),
                color = SecondaryTextColor,
                fontSize = 20.sp,
            )
        }

        Text(
            text = aqiData?.time?.s ?: stringResource(id = R.string.ellipses),
            color = SecondaryTextColor,
            fontSize = 14.sp,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .absolutePadding(left = 8.dp, right = 8.dp, top = 30.dp, bottom = 30.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.TopStart)
                    .padding(6.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp),
                    contentScale = ContentScale.Inside,
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_snow_cloud_night),
                    contentDescription = null
                )
                Text(
                    text = "Rainy light snow",
                    color = SecondaryTextColor,
                    fontSize = 14.sp,
                )
            }

            AirQualityCard(
                aqiValue = aqiData?.aqi,
                isLoading = isLoading,
                isInvalidData = false,
                onCardClick = onItemClick
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .padding(6.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "19\u00b0",
                        color = SecondaryTextColor,
                        fontSize = 36.sp,
                    )
                    Text(
                        text = stringResource(id = R.string.feels_like),
                        color = SecondaryTextColor,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "10\u00b0",
                        color = SecondaryTextColor,
                        fontSize = 20.sp,
                    )
                }
            }
        }

        Text(
            text = stringResource(id = R.string.pre_unhealthy_recomendation),
            fontSize = 14.sp,
            color = SecondaryTextColor,
//            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
        )

    }
}
