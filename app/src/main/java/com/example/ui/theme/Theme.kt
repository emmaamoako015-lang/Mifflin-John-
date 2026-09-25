package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AmberGoldPrimary,
    onPrimary = NavyDarkBackground,
    primaryContainer = NavyCardElevated,
    onPrimaryContainer = AmberGoldLight,
    secondary = ElectricBlue,
    onSecondary = TextWhitePrimary,
    secondaryContainer = NavyCardSurface,
    onSecondaryContainer = ElectricCyan,
    tertiary = MintEmerald,
    onTertiary = NavyDarkBackground,
    background = NavyDarkBackground,
    onBackground = TextWhitePrimary,
    surface = NavyDarkSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = NavyCardSurface,
    onSurfaceVariant = TextMuted,
    outline = NavyBorder,
    outlineVariant = NavyBorderLight
)

private val LightColorScheme = DarkColorScheme // Global Betting maintains a premium sportsbook dark interface

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NavyDarkBackground.toArgb()
                window.navigationBarColor = NavyDarkBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
