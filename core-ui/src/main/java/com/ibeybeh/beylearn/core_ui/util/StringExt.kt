package com.ibeybeh.beylearn.core_ui.util

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.BOLD_ITALIC
import android.graphics.Typeface.ITALIC
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.text.withStyle
import androidx.core.text.HtmlCompat

object StringExt {

    fun String.fromHtml() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)

    fun Spanned.toAnnotatedString(): AnnotatedString = buildAnnotatedString {
        val spanned = this@toAnnotatedString
        append(spanned.toString())
        getSpans(0, spanned.length, Any::class.java).forEach { span ->
            val start = getSpanStart(span)
            val end = getSpanEnd(span)
            when (span) {
                is StyleSpan -> when (span.style) {
                    BOLD -> addStyle(SpanStyle(fontWeight = SemiBold), start, end)
                    ITALIC -> addStyle(SpanStyle(fontStyle = Italic), start, end)
                    BOLD_ITALIC -> addStyle(
                        SpanStyle(fontWeight = Bold, fontStyle = Italic),
                        start,
                        end
                    )
                }

                is UnderlineSpan -> addStyle(SpanStyle(textDecoration = Underline), start, end)
                is ForegroundColorSpan -> addStyle(
                    SpanStyle(color = Color(span.foregroundColor)),
                    start,
                    end
                )
            }
        }
    }

    fun String.formatBoldText(
        words: List<String>,
        fontWeight: FontWeight = Bold,
        isUnderlineText: Boolean = false
    ) = buildAnnotatedString {
        var lastIndex = 0

        words.forEach { word ->
            val startIndex = this@formatBoldText.indexOf(word, lastIndex)
            if (startIndex != -1) {
                append(this@formatBoldText.substring(lastIndex, startIndex))
                if (isUnderlineText) {
                    withStyle(style = SpanStyle(textDecoration = Underline)) { append(word) }
                } else {
                    withStyle(style = SpanStyle(fontWeight = fontWeight)) { append(word) }
                }
                lastIndex = startIndex + word.length
            }
        }

        append(this@formatBoldText.substring(lastIndex))
    }
}