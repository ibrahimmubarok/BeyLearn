package com.ibeybeh.beylearn.api_test.data.remote.datasource

import com.ibeybeh.beylearn.api_test.data.remote.service.TestApi
import com.ibeybeh.beylearn.core.base.BaseRemoteDataSource
import javax.inject.Inject

class TestRemoteDataSourceImpl @Inject constructor(
    private val api: TestApi
) : TestRemoteDataSource, BaseRemoteDataSource() {
    override suspend fun getProfile() = safeApiCall { api.getProfile() }
}