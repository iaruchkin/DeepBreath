package com.iaruchkin.deepbreath.ui.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.tooling.preview.Preview
import com.iaruchkin.deepbreath.ui.theme.DeepBreathTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(title: String, openSearch: () -> Unit, openSettings: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Transparent,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(onClick = openSearch) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
        },
        actions = {
            IconButton(onClick = openSettings) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )
}

@Preview
@Composable
fun Preview() {
    DeepBreathTheme(false) {
        HomeAppBar("Title", {}, {})
    }
}
