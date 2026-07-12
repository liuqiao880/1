package com.taskflow.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.taskflow.app.domain.model.AccentColor

val InkBlack = Color(0xFF1A1A1A)
val InkGray = Color(0xFF4A4A4A)
val InkLight = Color(0xFF8A8A8A)
val InkVeryLight = Color(0xFFE8E8E8)

val NewspaperRed = Color(0xFFC41E3A)

val PaperWhite = Color(0xFFFAFAFA)
val PaperCream = Color(0xFFFDFBF7)
val PaperLightGray = Color(0xFFF5F5F5)

val LineSeparator = Color(0xFFE0E0E0)

val PriorityHigh = Color(0xFFC41E3A)
val PriorityMedium = Color(0xFFB8860B)
val PriorityLow = Color(0xFF4682B4)

fun getAccentColors(accent: AccentColor): Triple<Color, Color, Color> {
    return when (accent) {
        AccentColor.RED -> Triple(Color(0xFFC41E3A), Color(0xFF8B0000), Color(0xFFE63946))
        AccentColor.INK -> Triple(InkBlack, Color(0xFF000000), InkGray)
        AccentColor.GOLD -> Triple(Color(0xFFB8860B), Color(0xFF8B6508), Color(0xFFD4A017))
        AccentColor.BLUE -> Triple(Color(0xFF4682B4), Color(0xFF2F5F8F), Color(0xFF6FA8DC))
    }
}

fun getLightColorScheme(accent: AccentColor): androidx.compose.material3.ColorScheme {
    val (primary, _, _) = getAccentColors(accent)
    return lightColorScheme(
        primary = primary,
        onPrimary = Color.White,
        primaryContainer = primary.copy(alpha = 0.1f),
        onPrimaryContainer = InkBlack,
        secondary = InkGray,
        onSecondary = Color.White,
        tertiary = InkLight,
        background = PaperWhite,
        onBackground = InkBlack,
        surface = Color.White,
        onSurface = InkBlack,
        surfaceVariant = PaperLightGray,
        onSurfaceVariant = InkGray,
        error = Color(0xFFC41E3A),
        onError = Color.White,
        outline = LineSeparator,
    )
}

fun getDarkColorScheme(accent: AccentColor): androidx.compose.material3.ColorScheme {
    val (_, _, light) = getAccentColors(accent)
    return darkColorScheme(
        primary = light,
        onPrimary = Color.White,
        primaryContainer = light.copy(alpha = 0.15f),
        onPrimaryContainer = Color.White,
        secondary = InkLight,
        onSecondary = Color.White,
        tertiary = InkGray,
        background = InkBlack,
        onBackground = Color(0xFFF0F0F0),
        surface = Color(0xFF1E1E1E),
        onSurface = Color(0xFFF0F0F0),
        surfaceVariant = Color(0xFF2A2A2A),
        onSurfaceVariant = InkLight,
        error = Color(0xFFE63946),
        onError = Color.White,
        outline = Color(0xFF3A3A3A),
    )
}

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: AccentColor = AccentColor.RED,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) getDarkColorScheme(accentColor) else getLightColorScheme(accentColor)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
