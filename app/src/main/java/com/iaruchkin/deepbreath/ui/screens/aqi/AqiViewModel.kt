package com.iaruchkin.deepbreath.ui.screens.aqi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaruchkin.deepbreath.data.DataState
import com.iaruchkin.deepbreath.data.model.AqiCn
import com.iaruchkin.deepbreath.data.repository.AqiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AqiViewModel @Inject constructor(
    private val repo: AqiRepository
) : ViewModel() {
    val aqiDetailState: MutableState<DataState<AqiCn>?> = mutableStateOf(null)

    fun aqiDetailApi() {
        viewModelScope.launch {
            repo.aqiDetail().onEach {
                aqiDetailState.value = it
            }.launchIn(viewModelScope)
        }
    }
}