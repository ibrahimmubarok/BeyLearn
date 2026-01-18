package com.ibeybeh.beylearn.core.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtil {

    const val RAW_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ"

    fun String.asZonedDateTime(
        pattern: String = RAW_FORMAT
    ) = ZonedDateTime.parse(this, DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}