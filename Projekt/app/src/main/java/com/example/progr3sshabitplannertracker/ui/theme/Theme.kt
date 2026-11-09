package com.example.progr3sshabitplannertracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OceanBlue,
    secondary = LightIce,
    background = MidnightBg,
    surface = SurfaceBlue,
    onPrimary = IceWhite,
    onSecondary = MidnightBg,
    onBackground = IceWhite,
    onSurface = IceWhite
)

private val LightColorScheme = lightColorScheme(
    primary = NavyHighlight,
    secondary = OceanBlue,
    background = IceWhite,
    surface = LightIce,
    onPrimary = IceWhite,
    onSecondary = MidnightBg,
    onBackground = MidnightBg,
    onSurface = MidnightBg
)

@Composable
fun Progr3SSHabitPlannerTrackerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
