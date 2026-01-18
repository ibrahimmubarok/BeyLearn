package com.ibeybeh.beylearn.api_test.data.repository

import com.ibeybeh.beylearn.api_test.data.remote.model.GetProfileTestResponse
import com.ibeybeh.beylearn.core.remote.entity.ApiResult
import kotlinx.coroutines.flow.Flow

interface TestRepository {
    fun getProfile(): Flow<ApiResult<GetProfileTestResponse>>
}