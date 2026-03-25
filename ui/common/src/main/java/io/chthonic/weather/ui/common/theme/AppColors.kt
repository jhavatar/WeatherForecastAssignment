package io.chthonic.weather.ui.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// PALETTE — Warm Coastal
// All hex values copied exactly from the approved demo.
//
// DARK MODE
//   App background:       #080604  near-black, barely warm
//   Search bar:           #141008
//   Card (amber/hot):     #4A3C14  ← surfaceContainerHighest when hot
//   Card (teal/mild):     #0E3028  ← surfaceContainerHighest when cool
//   Card (red/extreme):   #380E08  ← surfaceContainerHighest when extreme
//   AppBar:               #3A2810
//   AppBar title:         #F0D8A8
//   AppBar accent (°C):   #F0A030
//   Icon box (hot):       #5A4818
//   Icon box (teal):      #143C30
//   Icon box (red):       #481408
//   City text:            #F0D8A8 (hot) / #A8D8C8 (teal) / #E8B8A0 (red)
//   Coords text:          #A08850 (hot) / #406858 (teal) / #784838 (red)
//   Temp color (hot):     #F0A030  amber
//   Temp color (mild):    #50C0A8  teal
//   Temp color (extreme): #E05030  red
//
// LIGHT MODE
//   App background:       #EDE7DA  warm parchment
//   Search bar:           #E0D8C8
//   Card background:      #F5EFE4  all cards same — temperature shown in icon+temp only
//   AppBar:               #C87840  burnt sienna
//   AppBar title:         #FFF0E0
//   AppBar accent (°C):   #FFE0B0
//   Icon box (hot):       #F0D090  golden
//   Icon box (teal):      #B8E0D4  seafoam
//   Icon box (red):       #F0B898  coral
//   City text:            #201808
//   Coords text:          #A08860
//   Temp color (hot):     #B86810  deep amber
//   Temp color (mild):    #207868  deep teal
//   Temp color (extreme): #C04020  deep red
// ─────────────────────────────────────────────────────────────────────────────

object AppColors {

    // ── Primary — amber / burnt orange (AppBar accent, hot temp, °C toggle) ──
    val Primary80 = Color(0xFFF0A030)   // dark: amber — temp hot, appbar accent
    val Primary40 = Color(0xFFC87840)   // light: burnt sienna — appbar bg
    val PrimaryContainer80 = Color(0xFF3A2810)   // dark: appbar bg
    val PrimaryContainer40 = Color(0xFFFFF0E0)   // light: appbar title bg
    val OnPrimaryContainer80 = Color(0xFFF0D8A8) // dark: appbar title text
    val OnPrimaryContainer40 = Color(0xFF201808) // light: on appbar

    // ── Secondary — teal (cool/mild temp, cool card bg) ───────────────────────
    val Secondary80 = Color(0xFF50C0A8) // dark: teal temp color
    val Secondary40 = Color(0xFF207868) // light: teal temp color
    val SecondaryContainer80 = Color(0xFF0E3028) // dark: teal card bg
    val SecondaryContainer40 = Color(0xFFB8E0D4) // light: teal icon box
    val OnSecondaryContainer80 = Color(0xFFA8D8C8)
    val OnSecondaryContainer40 = Color(0xFF0A2820)

    // ── Tertiary — red (extreme heat temp, red card bg) ───────────────────────
    val Tertiary80 = Color(0xFFE05030)  // dark: red temp color
    val Tertiary40 = Color(0xFFC04020)  // light: red temp color
    val TertiaryContainer80 = Color(0xFF380E08)  // dark: red card bg
    val TertiaryContainer40 = Color(0xFFF0B898)  // light: coral icon box
    val OnTertiaryContainer80 = Color(0xFFE8B8A0)
    val OnTertiaryContainer40 = Color(0xFF280A04)

    // ── Error ─────────────────────────────────────────────────────────────────
    val Error80 = Color(0xFFFFB4A2)
    val Error40 = Color(0xFFB3261E)
    val ErrorContainer80 = Color(0xFF8C1D18)
    val ErrorContainer40 = Color(0xFFF9DEDC)

    // ── DARK MODE ─────────────────────────────────────────────────────────────
    // NOTE: surfaceContainerHighest is the DEFAULT card color (amber/hot).
    // For cool and extreme cards, use temperatureCardColor() directly.
    val Surface_Dark = Color(0xFF080604)  // demo: app bg
    val SurfaceVariant_Dark = Color(0xFF1A1408)  // slightly lifted
    val SurfaceContainer_Dark = Color(0xFF141008)  // demo: search bar
    val SurfaceContainerHigh_Dark = Color(0xFF201808)  // mid step
    val SurfaceContainerHighest_Dark = Color(0xFF4A3C14)  // demo: amber card bg
    val SurfaceContainerLow_Dark = Color(0xFF100C06)
    val SurfaceContainerLowest_Dark = Color(0xFF060402)
    val InverseSurface_Dark = Color(0xFFEDE7DA)  // = Surface_Light
    val InverseOnSurface_Dark = Color(0xFF080604)

    // ── LIGHT MODE ────────────────────────────────────────────────────────────
    val Surface_Light = Color(0xFFEDE7DA)  // demo: app bg (parchment)
    val SurfaceVariant_Light = Color(0xFFE0D8C8)  // demo: search bar
    val SurfaceContainer_Light = Color(0xFFE8E0D0)
    val SurfaceContainerHigh_Light = Color(0xFFEEE8D8)
    val SurfaceContainerHighest_Light = Color(0xFFF5EFE4)  // demo: card bg
    val SurfaceContainerLow_Light = Color(0xFFF0EAE0)
    val SurfaceContainerLowest_Light = Color(0xFFFAF6F0)
    val InverseSurface_Light = Color(0xFF080604)  // = Surface_Dark
    val InverseOnSurface_Light = Color(0xFFEDE7DA)

    // ── On-colors dark ────────────────────────────────────────────────────────
    val OnPrimary_Dark = Color(0xFF1A0C00)
    val OnSecondary_Dark = Color(0xFF041C14)
    val OnTertiary_Dark = Color(0xFF180400)
    val OnSurface_Dark = Color(0xFFF0D8A8)  // demo: primary text on dark
    val OnSurfaceVariant_Dark = Color(0xFFA08850)  // demo: coords text
    val Outline_Dark = Color(0xFF705040)
    val OutlineVariant_Dark = Color(0xFF302010)
    val InversePrimary_Dark = Color(0xFFC87840)

    // ── On-colors light ───────────────────────────────────────────────────────
    val OnPrimary_Light = Color(0xFFFFF0E0)  // demo: appbar title
    val OnSecondary_Light = Color(0xFFFFFFFF)
    val OnTertiary_Light = Color(0xFFFFFFFF)
    val OnSurface_Light = Color(0xFF201808)  // demo: city text
    val OnSurfaceVariant_Light = Color(0xFFA08860)  // demo: coords text
    val Outline_Light = Color(0xFF907050)
    val OutlineVariant_Light = Color(0xFFD0C0A0)
    val InversePrimary_Light = Color(0xFFF0A030)

    val Scrim = Color(0xFF000000)

    @Composable
    fun appBarContainerColor(isDark: Boolean) = if (isDark) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.primary
    }

    @Composable
    fun appBarTitleContentColor(isDark: Boolean) = if (isDark) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
}


object AppColorTheme {

    private val DarkColorScheme = darkColorScheme(
        primary = AppColors.Primary80,            // #F0A030 amber
        onPrimary = AppColors.OnPrimary_Dark,
        primaryContainer = AppColors.PrimaryContainer80,   // #3A2810 appbar bg
        onPrimaryContainer = AppColors.OnPrimaryContainer80, // #F0D8A8 appbar title

        secondary = AppColors.Secondary80,          // #50C0A8 teal temp
        onSecondary = AppColors.OnSecondary_Dark,
        secondaryContainer = AppColors.SecondaryContainer80, // #0E3028 teal card
        onSecondaryContainer = AppColors.OnSecondaryContainer80,

        tertiary = AppColors.Tertiary80,           // #E05030 red temp
        onTertiary = AppColors.OnTertiary_Dark,
        tertiaryContainer = AppColors.TertiaryContainer80,  // #380E08 red card
        onTertiaryContainer = AppColors.OnTertiaryContainer80,

        error = AppColors.Error80,
        onError = Color(0xFF690005),
        errorContainer = AppColors.ErrorContainer80,
        onErrorContainer = Color(0xFFF9DEDC),

        background = AppColors.Surface_Dark,         // #080604
        onBackground = AppColors.OnSurface_Dark,
        surface = AppColors.Surface_Dark,
        onSurface = AppColors.OnSurface_Dark,       // #F0D8A8
        surfaceVariant = AppColors.SurfaceVariant_Dark,
        onSurfaceVariant = AppColors.OnSurfaceVariant_Dark,// #A08850

        surfaceContainer = AppColors.SurfaceContainer_Dark,       // #141008 search
        surfaceContainerHigh = AppColors.SurfaceContainerHigh_Dark,
        surfaceContainerHighest = AppColors.SurfaceContainerHighest_Dark, // #4A3C14 amber card
        surfaceContainerLow = AppColors.SurfaceContainerLow_Dark,
        surfaceContainerLowest = AppColors.SurfaceContainerLowest_Dark,

        outline = AppColors.Outline_Dark,
        outlineVariant = AppColors.OutlineVariant_Dark,
        scrim = AppColors.Scrim,
        inverseSurface = AppColors.InverseSurface_Dark,
        inverseOnSurface = AppColors.InverseOnSurface_Dark,
        inversePrimary = AppColors.InversePrimary_Dark,
    )

    private val LightColorScheme = lightColorScheme(
        primary = AppColors.Primary40,            // #C87840 burnt sienna
        onPrimary = AppColors.OnPrimary_Light,      // #FFF0E0
        primaryContainer = AppColors.PrimaryContainer40,   // #FFF0E0
        onPrimaryContainer = AppColors.OnPrimaryContainer40,

        secondary = AppColors.Secondary40,          // #207868 teal
        onSecondary = AppColors.OnSecondary_Light,
        secondaryContainer = AppColors.SecondaryContainer40, // #B8E0D4 teal icon box
        onSecondaryContainer = AppColors.OnSecondaryContainer40,

        tertiary = AppColors.Tertiary40,           // #C04020 red
        onTertiary = AppColors.OnTertiary_Light,
        tertiaryContainer = AppColors.TertiaryContainer40,  // #F0B898 coral icon box
        onTertiaryContainer = AppColors.OnTertiaryContainer40,

        error = AppColors.Error40,
        onError = Color(0xFFFFFFFF),
        errorContainer = AppColors.ErrorContainer40,
        onErrorContainer = Color(0xFF410E0B),

        background = AppColors.Surface_Light,        // #EDE7DA parchment
        onBackground = AppColors.OnSurface_Light,
        surface = AppColors.Surface_Light,
        onSurface = AppColors.OnSurface_Light,      // #201808
        surfaceVariant = AppColors.SurfaceVariant_Light, // #E0D8C8 search bar
        onSurfaceVariant = AppColors.OnSurfaceVariant_Light,// #A08860

        surfaceContainer = AppColors.SurfaceContainer_Light,
        surfaceContainerHigh = AppColors.SurfaceContainerHigh_Light,
        surfaceContainerHighest = AppColors.SurfaceContainerHighest_Light,// #F5EFE4 card
        surfaceContainerLow = AppColors.SurfaceContainerLow_Light,
        surfaceContainerLowest = AppColors.SurfaceContainerLowest_Light,

        outline = AppColors.Outline_Light,
        outlineVariant = AppColors.OutlineVariant_Light,
        scrim = AppColors.Scrim,
        inverseSurface = AppColors.InverseSurface_Light,
        inverseOnSurface = AppColors.InverseOnSurface_Light,
        inversePrimary = AppColors.InversePrimary_Light,
    )

    fun getColorScheme(isDarkTheme: Boolean): ColorScheme =
        if (isDarkTheme) DarkColorScheme else LightColorScheme
}