package com.ibeybeh.beylearn.core.base

import com.ibeybeh.beylearn.core.remote.entity.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseRepository {

    protected fun <T> proceedFlow(
        networkCall: suspend () -> ApiResult<T>
    ): Flow<ApiResult<T>> = flow {
        emit(ApiResult.Loading)
        val state = when (val result = networkCall()) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Empty<*> -> ApiResult.Empty()
            is ApiResult.Error<*> -> ApiResult.Error(result.data)
            is ApiResult.Loading -> ApiResult.Loading
        }
        emit(state)
    }.flowOn(Dispatchers.IO)
}