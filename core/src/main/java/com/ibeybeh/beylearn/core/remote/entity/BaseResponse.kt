package com.ibeybeh.beylearn.core.remote.entity

import com.google.gson.annotations.SerializedName

data class BaseResponse<A>(
    @SerializedName("code") val code: Int?,
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: A?,
)