package com.ibeybeh.beylearn.core.base

import com.ibeybeh.beylearn.core.remote.entity.ApiResult
import com.ibeybeh.beylearn.core.remote.entity.BaseErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseUseCase<in P, R> {
    operator fun invoke(params: P? = null): Flow<ApiResult<R>> = execute(params)

    protected abstract fun execute(params: P? = null): Flow<ApiResult<R>>
}

fun <T, R> Flow<ApiResult<T>>.mapToDomain(
    mapper: (T) -> R
): Flow<ApiResult<R>> {
    return this.map { result ->
        when (result) {
            is ApiResult.Success -> {
                try {
                    val domainData = mapper(result.data)
                    ApiResult.Success(domainData)
                } catch (e: Exception) {
                    ApiResult.Error(
                        BaseErrorResponse(
                            title = "Mapping Error",
                            message = e.message,
                            httpCode = -1
                        )
                    )
                }
            }

            is ApiResult.Error -> ApiResult.Error(result.data)
            is ApiResult.Empty -> ApiResult.Empty()
            is ApiResult.Loading -> ApiResult.Loading
        }
    }
}