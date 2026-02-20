package com.ibeybeh.beylearn.core_navigation.routes

import com.ibeybeh.beylearn.core_entity.Profile
import kotlinx.serialization.Serializable

@Serializable
object HomeNavGraph

@Serializable
object HomeRoute

@Serializable
data class DetailProfileRoute(val profile: Profile = Profile())