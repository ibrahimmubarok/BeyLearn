package com.ibeybeh.beylearn.core_ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Alignment.Companion.Center
import androidx.compose.ui.text.style.LineHeightStyle.Trim.Companion.None
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import com.ibeybeh.beylearn.core_ui.R
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp10
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp12
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp14
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp16
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp18
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp20
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp24
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp28
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp32
import com.ibeybeh.beylearn.core_ui.theme.TextDimen.Sp40

private val interphases = FontFamily(
    Font(R.font.interphases_regular, FontWeight.Normal),
    Font(R.font.interphases_medium, FontWeight.Medium),
    Font(R.font.interphases_bold, FontWeight.Bold),
    Font(R.font.interphases_demibold, FontWeight.SemiBold),
    Font(R.font.interphases_extrabold, FontWeight.ExtraBold)
)

private const val HEADLINE_LINE_HEIGHT = 1.2
private const val BODY_LINE_HEIGHT = 1.4
private const val LABEL_LINE_HEIGHT = 1.2
private const val AMOUNT_LINE_HEIGHT = 1.2
private const val AVATAR_LINE_HEIGHT = 1.2
private const val UNDERLINE_LINE_HEIGHT = 1.2

data class BeyLearnTypography(
    val H0: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Sp32,
        lineHeight = Sp32 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val H1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Sp28,
        lineHeight = Sp28 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val H2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp24,
        lineHeight = Sp24 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val H3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp18,
        lineHeight = Sp18 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val H4: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp16,
        lineHeight = Sp16 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val H5: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp14,
        lineHeight = Sp14 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val H6: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp12,
        lineHeight = Sp12 * HEADLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Body1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = Sp16,
        lineHeight = Sp16 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val BodyBold1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp16,
        lineHeight = Sp16 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Body2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = Sp14,
        lineHeight = Sp14 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val BodyBold2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp14,
        lineHeight = Sp14 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Body3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = Sp12,
        lineHeight = Sp12 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val BodyBold3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp12,
        lineHeight = Sp12 * BODY_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Label1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = Sp14,
        lineHeight = Sp14 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val LabelBold1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp14,
        lineHeight = Sp14 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Label2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = Sp12,
        lineHeight = Sp12 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val LabelBold2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp12,
        lineHeight = Sp12 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Label3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Normal,
        fontSize = Sp10,
        lineHeight = Sp10 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val LabelBold3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp10,
        lineHeight = Sp10 * LABEL_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Amount1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Sp40,
        lineHeight = Sp40 * AMOUNT_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Amount2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Sp28,
        lineHeight = Sp28 * AMOUNT_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Amount3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Sp20,
        lineHeight = Sp20 * AMOUNT_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Avatar1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.SemiBold,
        fontSize = Sp18,
        lineHeight = Sp18 * AVATAR_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None)
    ),
    val Underline1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = Sp16,
        lineHeight = Sp16 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val UnderlineBold1: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp16,
        lineHeight = Sp16 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val Underline2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = Sp14,
        lineHeight = Sp14 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val UnderlineBold2: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp14,
        lineHeight = Sp14 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val Underline3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Medium,
        fontSize = Sp12,
        lineHeight = Sp12 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    ),
    val UnderlineBold3: TextStyle = TextStyle(
        fontFamily = interphases,
        fontWeight = FontWeight.Bold,
        fontSize = Sp12,
        lineHeight = Sp12 * UNDERLINE_LINE_HEIGHT,
        lineHeightStyle = LineHeightStyle(alignment = Center, trim = None),
        textDecoration = Underline
    )
)

internal val LocalTypography = staticCompositionLocalOf { BeyLearnTypography() }