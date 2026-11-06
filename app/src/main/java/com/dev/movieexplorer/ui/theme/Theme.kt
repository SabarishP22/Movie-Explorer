package com.dev.movieexplorer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Amber,
    onPrimary = PaperBright,
    primaryContainer = AmberSoft,
    onPrimaryContainer = Ink,
    secondary = Moss,
    onSecondary = PaperBright,
    background = Paper,
    onBackground = Ink,
    surface = PaperBright,
    onSurface = Ink,
    surfaceVariant = Sand,
    onSurfaceVariant = Muted,
    outline = Line,
)

private val DarkColorScheme = darkColorScheme(
    primary = AmberDark,
    onPrimary = Color(0xFF3A1A00),
    primaryContainer = Color(0xFF6B3208),
    onPrimaryContainer = AmberSoft,
    secondary = Color(0xFF8FCFC0),
    onSecondary = Color(0xFF08332C),
    background = PaperDark,
    onBackground = InkDark,
    surface = SurfaceDark,
    onSurface = InkDark,
    surfaceVariant = Color(0xFF2C2823),
    onSurfaceVariant = Line,
    outline = Color(0xFF4A433B),
)

@Composable
fun MovieExplorerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
