package com.mahalatk.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/** Snapshot current [AppColor] values into a Material 3 [ColorScheme]. */
private fun buildColorScheme(dark: Boolean): ColorScheme {
    // Shared assignments — AppColor.isDark is already set, so getters
    // return the correct light/dark value for every property.
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = AppColor.Primary,
        onPrimary = AppColor.OnPrimary,
        primaryContainer = AppColor.PrimaryContainer,
        onPrimaryContainer = AppColor.OnPrimaryContainer,
        inversePrimary = AppColor.InversePrimary,
        secondary = AppColor.Secondary,
        onSecondary = AppColor.OnSecondary,
        secondaryContainer = AppColor.SecondaryContainer,
        onSecondaryContainer = AppColor.OnSecondaryContainer,
        tertiary = AppColor.Tertiary,
        onTertiary = AppColor.OnTertiary,
        tertiaryContainer = AppColor.TertiaryContainer,
        onTertiaryContainer = AppColor.Gray,
        background = AppColor.Background,
        onBackground = AppColor.Black,
        surface = AppColor.Surface,
        onSurface = AppColor.OnSurface,
        surfaceVariant = AppColor.SurfaceVariant,
        onSurfaceVariant = AppColor.OnSurfaceVariant,
        surfaceTint = AppColor.Primary,
        inverseSurface = AppColor.InverseSurface,
        inverseOnSurface = AppColor.InverseOnSurface,
        error = AppColor.Error,
        onError = AppColor.OnError,
        errorContainer = AppColor.ErrorContainer,
        onErrorContainer = AppColor.OnErrorContainer,
        outline = AppColor.Outline,
        outlineVariant = AppColor.OutlineVariant,
        scrim = AppColor.Scrim,
        surfaceBright = AppColor.SurfaceBright,
        surfaceDim = AppColor.SurfaceDim,
        surfaceContainer = AppColor.SurfaceContainer,
        surfaceContainerHigh = AppColor.SurfaceContainerHigh,
        surfaceContainerHighest = AppColor.SurfaceContainerHighest,
        surfaceContainerLow = AppColor.SurfaceContainerLow,
        surfaceContainerLowest = AppColor.SurfaceContainerLowest,
    )
}

@Composable
fun MahalatkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    AppColor.isDark = darkTheme
    val colorScheme = remember(darkTheme) { buildColorScheme(darkTheme) }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
