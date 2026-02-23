package com.ibeybeh.beylearn.core_ui.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

object ColorExt {
    fun String.toColor() = Color(this.toColorInt())
}