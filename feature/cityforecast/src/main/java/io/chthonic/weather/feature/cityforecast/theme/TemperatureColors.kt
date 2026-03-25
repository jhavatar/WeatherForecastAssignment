package io.chthonic.weather.feature.cityforecast.theme

import androidx.compose.ui.graphics.Color

internal object TemperatureColors {
    // ─────────────────────────────────────────────────────────────────────────────
    // TEMPERATURE CARD COLOR
    // Call this to get the card containerColor based on celsius temperature.
    // These are the exact card bg hex values from the demo.
    // ─────────────────────────────────────────────────────────────────────────────

    fun temperatureCardColor(celsius: Double?, isDark: Boolean): Color =
        if (isDark) {
            temperatureCardColorDark(celsius)
        } else {
            temperatureCardColorLight(celsius)
        }

    fun temperatureCardColorDark(celsius: Double?): Color = when {
        celsius == null -> Color(0xFF201808)   // neutral warm dark — surfaceContainerHigh
        celsius < 0 -> Color(0xFF0A2A38)   // deep blue — freezing
        celsius < 14 -> Color(0xFF0E3028)   // teal — cool    (demo exact)
        celsius < 24 -> Color(0xFF201808)   // neutral warm — mild
        celsius < 32 -> Color(0xFF4A3C14)   // amber — hot    (demo exact)
        else -> Color(0xFF380E08)   // red — extreme  (demo exact)
    }

    fun temperatureCardColorLight(celsius: Double?): Color = when {
        celsius == null -> Color(0xFFF5EFE4)   // neutral parchment
        celsius < 0 -> Color(0xFFE8F0EE)   // very faint teal tint
        celsius < 14 -> Color(0xFFECF0EE)   // faint teal
        celsius < 24 -> Color(0xFFF5EFE4)   // neutral — mild
        celsius < 32 -> Color(0xFFF5EDE0)   // faint amber tint
        else -> Color(0xFFF5E8E0)   // faint coral tint
    }

    // Icon box colors per temperature
    fun temperatureIconBoxColor(celsius: Double?, isDark: Boolean): Color =
        if (isDark) {
            temperatureIconBoxColorDark(celsius)
        } else {
            temperatureIconBoxColorLight(celsius)
        }

    fun temperatureIconBoxColorDark(celsius: Double?): Color = when {
        celsius == null -> Color(0xFF2A2010)   // neutral, no hue
        celsius < 14 -> Color(0xFF143C30)   // demo: teal icon box dark
        celsius < 32 -> Color(0xFF5A4818)   // demo: amber icon box dark
        else -> Color(0xFF481408)   // demo: red icon box dark
    }

    fun temperatureIconBoxColorLight(celsius: Double?): Color = when {
        celsius == null -> Color(0xFFE0D8C8)   // neutral — matches search bar, no hue
        celsius < 14 -> Color(0xFFB8E0D4)   // demo: seafoam icon box light
        celsius < 32 -> Color(0xFFF0D090)   // demo: golden icon box light
        else -> Color(0xFFF0B898)   // demo: coral icon box light
    }

    // Temperature text color
    fun temperatureTextColor(celsius: Double?, isDark: Boolean): Color =
        if (isDark) {
            temperatureTextColorDark(celsius)
        } else {
            temperatureTextColorLight(celsius)
        }

    fun temperatureTextColorDark(celsius: Double?): Color = when {
        celsius == null -> Color(0xFFA08850)   // muted coords color — unobtrusive
        celsius < 14 -> Color(0xFF50C0A8)   // demo: teal
        celsius < 32 -> Color(0xFFF0A030)   // demo: amber
        else -> Color(0xFFE05030)   // demo: red
    }

    fun temperatureTextColorLight(celsius: Double?): Color = when {
        celsius == null -> Color(0xFFA08860)   // muted coords color — unobtrusive
        celsius < 14 -> Color(0xFF207868)   // demo: deep teal
        celsius < 32 -> Color(0xFFB86810)   // demo: deep amber
        else -> Color(0xFFC04020)   // demo: deep red
    }

    // Temperature Icon color
    fun temperatureIconTint(celsius: Double?, isDark: Boolean): Color =
        temperatureTextColor(celsius, isDark)
}