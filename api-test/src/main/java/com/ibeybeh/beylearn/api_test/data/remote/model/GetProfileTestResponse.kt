package com.ibeybeh.beylearn.api_test.data.remote.model

import kotlinx.serialization.SerialName

data class GetProfileTestResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("age") val age: Int,
    @SerialName("city") val city: String,
    @SerialName("country") val country: String
)