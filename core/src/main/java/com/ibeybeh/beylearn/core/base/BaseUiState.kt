package com.ibeybeh.beylearn.core.base

import com.ibeybeh.beylearn.core.remote.entity.BaseErrorResponse

sealed class BaseUiState<out T> {
    object Loading : BaseUiState<Nothing>()
    data class Success<T>(val data: T) : BaseUiState<T>()
    object Empty : BaseUiState<Nothing>()
    data class Error(val error: BaseErrorResponse) : BaseUiState<Nothing>()
}