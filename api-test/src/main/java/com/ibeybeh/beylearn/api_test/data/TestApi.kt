package com.ibeybeh.beylearn.api_test.data

import retrofit2.http.GET

interface TestApi {

    @GET("profile")
    suspend fun getProfile()
}