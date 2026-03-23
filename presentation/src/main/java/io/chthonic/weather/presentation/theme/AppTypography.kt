package io.chthonic.weather.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.chthonic.weather.presentation.R

val AppTypography: Typography by lazy {

    val outfit = FontFamily(
        Font(
            resId = R.font.outfit_thin,
            weight = FontWeight.W100
        ),
        Font(
            resId = R.font.outfit_extralight,
            weight = FontWeight.W200
        ),
        Font(
            resId = R.font.outfit_light,
            weight = FontWeight.W300
        ),
        Font(
            resId = R.font.outfit_regular,
            weight = FontWeight.W400
        ),
        Font(
            resId = R.font.outfit_medium,
            weight = FontWeight.W500
        ),
        Font(
            resId = R.font.outfit_semibold,
            weight = FontWeight.W600
        ),
        Font(
            resId = R.font.outfit_bold,
            weight = FontWeight.W700
        ),
        Font(
            resId = R.font.outfit_extrabold,
            weight = FontWeight.W800
        ),
        Font(
            resId = R.font.outfit_black,
            weight = FontWeight.W900
        ),
    )

    Typography(
        // Main temperature display — the big number
        displayLarge = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Black,
            fontSize = 72.sp,
            lineHeight = 72.sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Black,
            fontSize = 56.sp,
            lineHeight = 56.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Black,
            fontSize = 44.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),

        // Section headers
        headlineLarge = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            letterSpacing = 0.sp
        ),

        // Card titles, city names
        titleLarge = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        ),

        // Descriptions, conditions
        bodyLarge = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.sp
        ),

        // Appbar, coords, chips, uppercase labels
        labelLarge = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 1.5.sp
        ),
        labelMedium = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 1.2.sp
        ),
        labelSmall = TextStyle(
            fontFamily = outfit,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            letterSpacing = 1.sp
        ),
    )
}

