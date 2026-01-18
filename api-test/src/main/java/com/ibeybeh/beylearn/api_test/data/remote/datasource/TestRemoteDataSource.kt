package com.ibeybeh.beylearn.api_test.data.remote.datasource

import com.ibeybeh.beylearn.api_test.data.remote.model.GetProfileTestResponse
import com.ibeybeh.beylearn.core.remote.entity.ApiResult

interface TestRemoteDataSource {
    suspend fun getProfile(): ApiResult<GetProfileTestResponse>
}