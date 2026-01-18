package com.ibeybeh.beylearn.core.base

fun interface BaseMapper<in From, out To> {
    fun map(from: From): To
}