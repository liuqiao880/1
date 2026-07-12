package com.taskflow.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NewspaperRed = Color(0xFFC41E3A)
val NewspaperRedDark = Color(0xFF8B0000)
val NewspaperRedLight = Color(0xFFE63946)

val InkBlack = Color(0xFF1A1A1A)
val InkGray = Color(0xFF4A4A4A)
val InkLight = Color(0xFF8A8A8A)
val InkVeryLight = Color(0xFFE8E8E8)

val PaperWhite = Color(0xFFFAFAFA)
val PaperCream = Color(0xFFFDFBF7)
val PaperLightGray = Color(0xFFF5F5F5)

val LineSeparator = Color(0xFFE0E0E0)

private val LightColorScheme = lightColorScheme(
    primary = NewspaperRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEBEE),
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
    error = NewspaperRed,
    onError = Color.White,
    outline = LineSeparator,
)

private val DarkColorScheme = darkColorScheme(
    primary = NewspaperRedLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3D0000),
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
    error = NewspaperRedLight,
    onError = Color.White,
    outline = Color(0xFF3A3A3A),
)

@Composable
fun TaskFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
