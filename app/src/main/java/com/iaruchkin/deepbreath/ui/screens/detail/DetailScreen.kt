package com.iaruchkin.deepbreath.ui.screens.detail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController, id: Int) {
    val detailViewModel = hiltViewModel<DetailViewModel>()

    Text(text = "hello nav graph")

}

@Preview(name = "Detail", showBackground = true)
@Composable
fun Preview() {

}