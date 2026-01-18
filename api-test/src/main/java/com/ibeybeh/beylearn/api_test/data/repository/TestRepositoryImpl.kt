package com.ibeybeh.beylearn.api_test.data.repository

import com.ibeybeh.beylearn.api_test.data.remote.datasource.TestRemoteDataSource
import com.ibeybeh.beylearn.api_test.data.remote.model.GetProfileTestResponse
import com.ibeybeh.beylearn.core.base.BaseRepository
import com.ibeybeh.beylearn.core.remote.entity.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testRemoteDataSource: TestRemoteDataSource
) : TestRepository, BaseRepository() {
    override fun getProfile(): Flow<ApiResult<GetProfileTestResponse>> {
        return proceedFlow { testRemoteDataSource.getProfile() }
    }
}