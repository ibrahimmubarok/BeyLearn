package com.ibeybeh.beylearn.core.remote.entity

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error<out T>(val data: BaseErrorResponse) : ApiResult<T>()
    class Empty<out T> : ApiResult<T>()
    object Loading : ApiResult<Nothing>()
}