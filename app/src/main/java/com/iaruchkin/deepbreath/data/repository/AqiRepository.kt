package com.iaruchkin.deepbreath.data.repository

import com.iaruchkin.deepbreath.data.DataState
import com.iaruchkin.deepbreath.data.datasource.remote.ApiService
import com.iaruchkin.deepbreath.data.model.AqiCn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AqiRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun aqiDetail(): Flow<DataState<AqiCn>> = flow {
        emit(DataState.Loading)
        try {
            val requestResult = apiService.aqiDetail()
            emit(DataState.Success(requestResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

}