package com.ibeybeh.beylearn.core.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber

object JsonUtil {
    val gson: Gson = GsonBuilder().create()

    fun <T> toJson(data: T): String = gson.toJson(data)

    inline fun <reified T> fromJson(json: String): T? = try {
        gson.fromJson(json, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        Timber.tag("JsonUtil").e(e)
        null
    }
}