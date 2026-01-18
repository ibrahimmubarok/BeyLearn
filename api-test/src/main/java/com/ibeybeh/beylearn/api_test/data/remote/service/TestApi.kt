package com.ibeybeh.beylearn.api_test.data.remote.service

import com.ibeybeh.beylearn.api_test.data.remote.model.GetProfileTestResponse
import com.ibeybeh.beylearn.core.remote.entity.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface TestApi {

    @GET("profile")
    suspend fun getProfile(): Response<BaseResponse<GetProfileTestResponse>>
}