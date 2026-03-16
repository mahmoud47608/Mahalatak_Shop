package com.aait.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun colorPrimary() = MaterialTheme.colorScheme.primary

@Composable
fun colorPrimaryAlpha_10() = Color(0x1A4E9FE0)

@Composable
fun colorText() = MaterialTheme.colorScheme.onBackground

@Composable
fun colorTextHint() = MaterialTheme.colorScheme.tertiary

@Composable
fun colorWhite() = Color.White

object ColorLightTokens {
    // Primary Blue (from Figma VIVO)
    val Primary = Color(0xFF4E9FE0)
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFFEDF5FC)  // Foundation/Blue/Light
    val OnPrimaryContainer = Color(0xFF1A3A5C)
    val PrimaryHover = Color(0xFFE4F1FA)      // Foundation/Blue/Light:hover
    val PrimaryActive = Color(0xFFC8E1F5)     // Foundation/Blue/Light:active

    // Secondary (Purple from button gradient)
    val Secondary = Color(0xFF543592)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFFEDE7F6)
    val OnSecondaryContainer = Color(0xFF2A2D2D)

    // Tertiary (Text Hint - FontSmall)
    val Tertiary = Color(0xFF999999)
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer = Color(0xFFF5F5F5)
    val OnTertiaryContainer = Color(0xFF555555)

    // Backgrounds (from Figma VIVO)
    val Background = Color(0xFFF4F8FB)        // BG
    val OnBackground = Color(0xFF2A2D2D)      // Font Bold

    // Surfaces
    val Surface = Color(0xFFFFFFFF)           // White
    val OnSurface = Color(0xFF2A2D2D)         // Font Bold
    val SurfaceVariant = Color(0xFFFBFBFB)    // Foundation/Grey/Light
    val OnSurfaceVariant = Color(0xFF555555)  // Font Regular
    val CardBackground = Color(0xFFF2F9FF)    // Card background

    val InverseSurface = Color(0xFF2A2D2D)
    val InverseOnSurface = Color(0xFFFFFFFF)
    val InversePrimary = Color(0xFF4E9FE0)

    // Semantic Colors (from Figma)
    val Error = Color(0xFFE63946)             // Danger
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFFE5E7)
    val OnErrorContainer = Color(0xFF8C1D24)

    val Success = Color(0xFF249F58)           // Green
    val OnSuccess = Color(0xFFFFFFFF)

    val Warning = Color(0xFFFFC107)
    val OnWarning = Color(0xFF2A2D2D)

    val Info = Color(0xFF42A5F5)
    val OnInfo = Color(0xFFFFFFFF)

    // Borders & Lines (from Figma VIVO)
    val Outline = Color(0xFFEEEEF6)           // Input border color
    val OutlineVariant = Color(0xFFF5F5F5)    // Border
    val Border = Color(0xFFEEEEF6)            // Input border
    val Line = Color(0xFFE6E6E6)
    val Scrim = Color(0xFF000000)

    // Surface containers
    val SurfaceBright = Color(0xFFFFFFFF)
    val SurfaceDim = Color(0xFFF4F8FB)
    val SurfaceContainer = Color(0xFFFBFBFB)
    val SurfaceContainerLow = Color(0xFFFFFFFF)
    val SurfaceContainerLowest = Color(0xFFFFFFFF)
    val SurfaceContainerHigh = Color(0xFFF2F9FF)
    val SurfaceContainerHighest = Color(0xFFEDF5FC)

    // Text Colors (from Figma VIVO)
    val TextPrimary = Color(0xFF2A2D2D)       // Font Bold
    val TextBold = Color(0xFF2A2D2D)          // Font Bold
    val TextRegular = Color(0xFF555555)       // Font Regular
    val TextHint = Color(0xFF999999)          // FontSmall

    // Icon Colors
    val IconColor = Color(0xFF888888)         // رصاصي خفيف
    val LightGray = Color(0xFFA8A8A8)         // light gray

    // Additional Colors from Figma
    val NavyBlue = Color(0xFF1A47B8)
    val SupportiveOrange = Color(0xFFFFB319)  // Supportive 4
    val SecondaryGreen = Color(0xFF53BE98)
    val Purple = Color(0xFF543592)            // Button gradient color

    // Legacy placeholder color
    val PlaceholderColor = Color(0xFF999999)
}

object ColorDarkTokens {
    // Primary Blue (lightened for dark theme)
    val Primary = Color(0xFF6BB3E8)
    val OnPrimary = Color(0xFF1A3A5C)
    val PrimaryContainer = Color(0xFF2A5580)
    val OnPrimaryContainer = Color(0xFFE4F1FA)
    val PrimaryHover = Color(0xFF3D6A94)
    val PrimaryActive = Color(0xFF4E7BA6)

    // Secondary (Purple lightened for dark theme)
    val Secondary = Color(0xFF7B5AB8)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFF3D2A6B)
    val OnSecondaryContainer = Color(0xFFEDE7F6)

    // Tertiary
    val Tertiary = Color(0xFF808080)
    val OnTertiary = Color(0xFF1A1A1A)
    val TertiaryContainer = Color(0xFF3A3A3A)
    val OnTertiaryContainer = Color(0xFFCCCCCC)

    // Backgrounds
    val Background = Color(0xFF0F1214)
    val OnBackground = Color(0xFFF5F5F5)

    // Surfaces
    val Surface = Color(0xFF1A1D20)
    val OnSurface = Color(0xFFF5F5F5)
    val SurfaceVariant = Color(0xFF252A2E)
    val OnSurfaceVariant = Color(0xFFCCCCCC)
    val CardBackground = Color(0xFF1F2428)

    val InverseSurface = Color(0xFFF4F8FB)
    val InverseOnSurface = Color(0xFF2A2D2D)
    val InversePrimary = Color(0xFF4E9FE0)

    // Semantic Colors
    val Error = Color(0xFFFF6B75)
    val OnError = Color(0xFF1A0507)
    val ErrorContainer = Color(0xFF8C1D24)
    val OnErrorContainer = Color(0xFFFFE5E7)

    val Success = Color(0xFF4ACD77)
    val OnSuccess = Color(0xFF0D2818)

    val Warning = Color(0xFFFFD54F)
    val OnWarning = Color(0xFF2A2D2D)

    val Info = Color(0xFF64B5F6)
    val OnInfo = Color(0xFF0D2A3F)

    // Borders & Lines
    val Outline = Color(0xFF404040)
    val OutlineVariant = Color(0xFF333333)
    val Border = Color(0xFF333333)
    val Line = Color(0xFF404040)
    val Scrim = Color(0xFF000000)

    // Surface containers
    val SurfaceBright = Color(0xFF2A2E32)
    val SurfaceDim = Color(0xFF141618)
    val SurfaceContainer = Color(0xFF1E2124)
    val SurfaceContainerLow = Color(0xFF161819)
    val SurfaceContainerLowest = Color(0xFF0D0E0F)
    val SurfaceContainerHigh = Color(0xFF252A2E)
    val SurfaceContainerHighest = Color(0xFF2E3438)

    // Text Colors
    val TextPrimary = Color(0xFFF5F5F5)
    val TextBold = Color(0xFFFFFFFF)
    val TextRegular = Color(0xFFCCCCCC)
    val TextHint = Color(0xFF808080)

    // Icon Colors
    val IconColor = Color(0xFFA0A0A0)
    val LightGray = Color(0xFF707070)

    // Additional Colors
    val NavyBlue = Color(0xFF5A7FD6)
    val SupportiveOrange = Color(0xFFFFCC4D)
    val SecondaryGreen = Color(0xFF7AD4B3)
    val Purple = Color(0xFF7B5AB8)

    // Legacy placeholder color
    val PlaceholderColor = Color(0xFF808080)
}
