package com.aait.base.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Extended color scheme for custom Figma VIVO colors not covered by Material3
 */
data class ExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color,
    val textPrimary: Color,
    val textBold: Color,
    val textRegular: Color,
    val textHint: Color,
    val iconColor: Color,
    val lightGray: Color,
    val border: Color,
    val line: Color,
    val cardBackground: Color,
    val primaryHover: Color,
    val primaryActive: Color,
    val navyBlue: Color,
    val supportiveOrange: Color,
    val secondaryGreen: Color,
    val purple: Color
)

private val LightExtendedColors = ExtendedColors(
    success = ColorLightTokens.Success,
    onSuccess = ColorLightTokens.OnSuccess,
    warning = ColorLightTokens.Warning,
    onWarning = ColorLightTokens.OnWarning,
    info = ColorLightTokens.Info,
    onInfo = ColorLightTokens.OnInfo,
    textPrimary = ColorLightTokens.TextPrimary,
    textBold = ColorLightTokens.TextBold,
    textRegular = ColorLightTokens.TextRegular,
    textHint = ColorLightTokens.TextHint,
    iconColor = ColorLightTokens.IconColor,
    lightGray = ColorLightTokens.LightGray,
    border = ColorLightTokens.Border,
    line = ColorLightTokens.Line,
    cardBackground = ColorLightTokens.CardBackground,
    primaryHover = ColorLightTokens.PrimaryHover,
    primaryActive = ColorLightTokens.PrimaryActive,
    navyBlue = ColorLightTokens.NavyBlue,
    supportiveOrange = ColorLightTokens.SupportiveOrange,
    secondaryGreen = ColorLightTokens.SecondaryGreen,
    purple = ColorLightTokens.Purple
)

private val DarkExtendedColors = ExtendedColors(
    success = ColorDarkTokens.Success,
    onSuccess = ColorDarkTokens.OnSuccess,
    warning = ColorDarkTokens.Warning,
    onWarning = ColorDarkTokens.OnWarning,
    info = ColorDarkTokens.Info,
    onInfo = ColorDarkTokens.OnInfo,
    textPrimary = ColorDarkTokens.TextPrimary,
    textBold = ColorDarkTokens.TextBold,
    textRegular = ColorDarkTokens.TextRegular,
    textHint = ColorDarkTokens.TextHint,
    iconColor = ColorDarkTokens.IconColor,
    lightGray = ColorDarkTokens.LightGray,
    border = ColorDarkTokens.Border,
    line = ColorDarkTokens.Line,
    cardBackground = ColorDarkTokens.CardBackground,
    primaryHover = ColorDarkTokens.PrimaryHover,
    primaryActive = ColorDarkTokens.PrimaryActive,
    navyBlue = ColorDarkTokens.NavyBlue,
    supportiveOrange = ColorDarkTokens.SupportiveOrange,
    secondaryGreen = ColorDarkTokens.SecondaryGreen,
    purple = ColorDarkTokens.Purple
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

/**
 * Access extended colors from the current theme
 */
object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
}

@Composable
fun BaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color disabled by default to use Figma design system colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = ColorDarkTokens.Primary,
    onPrimary = ColorDarkTokens.OnPrimary,
    primaryContainer = ColorDarkTokens.PrimaryContainer,
    onPrimaryContainer = ColorDarkTokens.OnPrimaryContainer,
    inversePrimary = ColorDarkTokens.InversePrimary,
    secondary = ColorDarkTokens.Secondary,
    onSecondary = ColorDarkTokens.OnSecondary,
    secondaryContainer = ColorDarkTokens.SecondaryContainer,
    onSecondaryContainer = ColorDarkTokens.OnSecondaryContainer,
    tertiary = ColorDarkTokens.Tertiary,
    onTertiary = ColorDarkTokens.OnTertiary,
    tertiaryContainer = ColorDarkTokens.TertiaryContainer,
    onTertiaryContainer = ColorDarkTokens.OnTertiaryContainer,
    background = ColorDarkTokens.Background,
    onBackground = ColorDarkTokens.OnBackground,
    surface = ColorDarkTokens.Surface,
    onSurface = ColorDarkTokens.OnSurface,
    surfaceVariant = ColorDarkTokens.SurfaceVariant,
    onSurfaceVariant = ColorDarkTokens.OnSurfaceVariant,
    surfaceTint = ColorDarkTokens.Primary,
    inverseSurface = ColorDarkTokens.InverseSurface,
    inverseOnSurface = ColorDarkTokens.InverseOnSurface,
    error = ColorDarkTokens.Error,
    onError = ColorDarkTokens.OnError,
    errorContainer = ColorDarkTokens.ErrorContainer,
    onErrorContainer = ColorDarkTokens.OnErrorContainer,
    outline = ColorDarkTokens.Outline,
    outlineVariant = ColorDarkTokens.OutlineVariant,
    scrim = ColorDarkTokens.Scrim,
    surfaceBright = ColorDarkTokens.SurfaceBright,
    surfaceContainer = ColorDarkTokens.SurfaceContainer,
    surfaceContainerHigh = ColorDarkTokens.SurfaceContainerHigh,
    surfaceContainerHighest = ColorDarkTokens.SurfaceContainerHighest,
    surfaceContainerLow = ColorDarkTokens.SurfaceContainerLow,
    surfaceContainerLowest = ColorDarkTokens.SurfaceContainerLowest,
    surfaceDim = ColorDarkTokens.SurfaceDim,
)

private val LightColorScheme = lightColorScheme(
    primary = ColorLightTokens.Primary,
    onPrimary = ColorLightTokens.OnPrimary,
    primaryContainer = ColorLightTokens.PrimaryContainer,
    onPrimaryContainer = ColorLightTokens.OnPrimaryContainer,
    inversePrimary = ColorLightTokens.InversePrimary,
    secondary = ColorLightTokens.Secondary,
    onSecondary = ColorLightTokens.OnSecondary,
    secondaryContainer = ColorLightTokens.SecondaryContainer,
    onSecondaryContainer = ColorLightTokens.OnSecondaryContainer,
    tertiary = ColorLightTokens.Tertiary,
    onTertiary = ColorLightTokens.OnTertiary,
    tertiaryContainer = ColorLightTokens.TertiaryContainer,
    onTertiaryContainer = ColorLightTokens.OnTertiaryContainer,
    background = ColorLightTokens.Background,
    onBackground = ColorLightTokens.OnBackground,
    surface = ColorLightTokens.Surface,
    onSurface = ColorLightTokens.OnSurface,
    surfaceVariant = ColorLightTokens.SurfaceVariant,
    onSurfaceVariant = ColorLightTokens.OnSurfaceVariant,
    surfaceTint = ColorLightTokens.Primary,
    inverseSurface = ColorLightTokens.InverseSurface,
    inverseOnSurface = ColorLightTokens.InverseOnSurface,
    error = ColorLightTokens.Error,
    onError = ColorLightTokens.OnError,
    errorContainer = ColorLightTokens.ErrorContainer,
    onErrorContainer = ColorLightTokens.OnErrorContainer,
    outline = ColorLightTokens.Outline,
    outlineVariant = ColorLightTokens.OutlineVariant,
    scrim = ColorLightTokens.Scrim,
    surfaceBright = ColorLightTokens.SurfaceBright,
    surfaceContainer = ColorLightTokens.SurfaceContainer,
    surfaceContainerHigh = ColorLightTokens.SurfaceContainerHigh,
    surfaceContainerHighest = ColorLightTokens.SurfaceContainerHighest,
    surfaceContainerLow = ColorLightTokens.SurfaceContainerLow,
    surfaceContainerLowest = ColorLightTokens.SurfaceContainerLowest,
    surfaceDim = ColorLightTokens.SurfaceDim,
)
