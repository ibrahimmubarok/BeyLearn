package com.ibeybeh.beylearn.core_entity

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int,
    val name: String,
    val email: String,
    val age: Int,
    val city: String,
    val country: String
)