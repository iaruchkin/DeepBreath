package com.iaruchkin.deepbreath.ui.screens.search

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SearchScreen(navController: NavController) {
    val searchViewModel = hiltViewModel<SearchViewModel>()

    Text(text = "TODO Search screen")

}

@Preview(name = "Search", showBackground = true)
@Composable
fun Preview() {

}