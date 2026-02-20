package com.ibeybeh.beylearn.core_entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Profile(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    val city: String = "",
    val country: String = ""
) : Parcelable