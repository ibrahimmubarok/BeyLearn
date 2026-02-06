package com.ibeybeh.beylearn.core_navigation.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class BeyDestination(
    val route: KClass<*>,
    val parentGraph: KClass<*>,
    val isStartDestination: Boolean = false
)