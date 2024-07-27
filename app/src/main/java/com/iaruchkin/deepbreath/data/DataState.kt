package com.iaruchkin.deepbreath.data

/**
 * Data state for processing api response Loading, Success and Error
 */
sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
}

fun <T : Any> T.wrapSuccess(): DataState<T> = DataState.Success(this)
fun <T : Any> T.wrapError(): DataState<T> = DataState.Error(Exception())
fun <T : Any> T.wrapLoading(): DataState<T> = DataState.Loading
