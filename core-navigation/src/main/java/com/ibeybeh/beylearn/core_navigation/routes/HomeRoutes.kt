package com.ibeybeh.beylearn.core_navigation.routes

import kotlinx.serialization.Serializable

@Serializable
object HomeNavGraph

@Serializable
object HomeRoute

@Serializable
data class DetailProfile(val profile: String)