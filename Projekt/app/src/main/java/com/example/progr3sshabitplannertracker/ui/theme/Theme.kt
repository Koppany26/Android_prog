package com.example.progr3sshabitplannertracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SageGreen,
    secondary = SandGold,
    background = DarkForest,
    surface = ForestGreen,
    onPrimary = CreamOffWhite,
    onSecondary = DarkForest,
    onBackground = CreamOffWhite,
    onSurface = CreamOffWhite
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = SageGreen,
    background = CreamOffWhite,
    surface = SandGold,
    onPrimary = CreamOffWhite,
    onSecondary = CreamOffWhite,
    onBackground = DarkForest,
    onSurface = DarkForest
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
