package com.mahalatk.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
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
    surfaceContainer = AppColor.SurfaceContainer,
    surfaceContainerHigh = AppColor.SurfaceContainerHigh,
    surfaceContainerHighest = AppColor.SurfaceContainerHighest,
    surfaceContainerLow = AppColor.SurfaceContainerLow,
    surfaceContainerLowest = AppColor.SurfaceContainerLowest,
    surfaceDim = AppColor.SurfaceDim,
)

private val DarkColorScheme = darkColorScheme(
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
    surfaceContainer = AppColor.SurfaceContainer,
    surfaceContainerHigh = AppColor.SurfaceContainerHigh,
    surfaceContainerHighest = AppColor.SurfaceContainerHighest,
    surfaceContainerLow = AppColor.SurfaceContainerLow,
    surfaceContainerLowest = AppColor.SurfaceContainerLowest,
    surfaceDim = AppColor.SurfaceDim,
)

@Composable
fun MahalatkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
