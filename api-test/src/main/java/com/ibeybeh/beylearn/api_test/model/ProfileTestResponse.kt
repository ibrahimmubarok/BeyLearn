package com.ibeybeh.beylearn.api_test.model

import kotlinx.serialization.SerialName

data class ProfileTestResponseResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @SerialName("age") val age: Int,
    @SerialName("city") val city: String,
    @SerialName("country") val country: String
)