package com.ibeybeh.beylearn.core.remote.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseErrorResponse(
    @SerializedName("errorTitle") val title: String? = null,
    @SerializedName("errorMessage") val message: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("httpCode") val httpCode: Int? = null
) : Parcelable