package com.mahalatk.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.mahalatk.theme.AppColor.isDark
import com.mahalatk.theme.AppColor.of

/**
 * Single source of truth for app colors. Reactive to dark mode.
 *
 * - [isDark] is Compose State — flipping it recomposes every reader.
 * - Theme-aware colors use [of] which picks light/dark in one call.
 * - [Color] is a value class (ULong) — zero heap allocation per read.
 */
object AppColor {

    var isDark by mutableStateOf(false)

    /** Pick [light] or [dark] based on current theme. */
    private fun of(light: Color, dark: Color): Color = if (isDark) dark else light

    // ── Primary ──────────────────────────────────────
    val Primary get() = of(Color(0xFF5AA6AC), Color(0xFF1F6268))
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer get() = of(Color(0xFFE0F5F1), Color(0xFF153033))
    val OnPrimaryContainer get() = of(Color(0xFF5AA6AC), Color(0xFF6BAFB5))
    val InversePrimary = Color(0xFF1B8A7A)

    // ── Secondary ────────────────────────────────────
    val Secondary = Color(0x805AA6AC)
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer get() = of(Color(0xFFEDE7F6), Color(0xFF2A3A3B))
    val OnSecondaryContainer get() = of(Color(0xFF2A2D2D), Color(0xFFE0E0E0))

    // ── Tertiary ─────────────────────────────────────
    val Tertiary = Color(0xFF999999)
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer get() = of(Color(0xFFF5F5F5), Color(0xFF2A2A2A))
    val Gray = Color(0xFFCCCCCC)

    // ── Background & Surface ─────────────────────────
    val Background get() = of(Color(0xFFF4F8FB), Color(0xFF121212))
    val Black get() = of(Color(0xFF2A2D2D), Color(0xFFE0E0E0))
    val Surface get() = of(Color(0xFFFFFFFF), Color(0xFF1E1E1E))
    val OnSurface get() = of(Color(0xFF2A2D2D), Color(0xFFE0E0E0))
    val SurfaceVariant get() = of(Color(0xFFFBFBFB), Color(0xFF2A2A2A))
    val OnSurfaceVariant get() = of(Color(0xFF555555), Color(0xFFB0B0B0))
    val InverseSurface get() = of(Color(0xFF2A2D2D), Color(0xFFE0E0E0))
    val InverseOnSurface get() = of(Color(0xFFFFFFFF), Color(0xFF1E1E1E))

    // ── Error ────────────────────────────────────────
    val Error = Color(0xFFE63946)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer get() = of(Color(0xFFFFE5E7), Color(0xFF3D1418))
    val OnErrorContainer get() = of(Color(0xFF8C1D24), Color(0xFFFFB3B8))

    // ── Text ─────────────────────────────────────────
    val TextPrimary get() = of(Color(0xFF2A2D2D), Color(0xFFE0E0E0))
    val TextSecondary get() = of(Color(0xFF555555), Color(0xFFB0B0B0))
    val TextHint get() = of(Color(0xFF999999), Color(0xFF757575))

    // ── Screen & Card ────────────────────────────────
    val ScreenBackground get() = of(Color(0xFFF4F8FB), Color(0xFF121212))
    val CardBackground get() = of(Color(0xFFF2F9FF), Color(0xFF1E1E1E))
    val CardBorder get() = of(Color(0xFFEEEEF6), Color(0xFF2A2A2A))

    // ── Borders & Lines ──────────────────────────────
    val Outline get() = of(Color(0xFFEEEEF6), Color(0xFF2A2A2A))
    val OutlineVariant get() = of(Color(0xFFF5F5F5), Color(0xFF333333))
    val Border get() = of(Color(0xFFEEEEF6), Color(0xFF2A2A2A))
    val Line get() = of(Color(0xFFE6E6E6), Color(0xFF333333))
    val Scrim = Color(0xFF000000)

    // ── Surface Containers ───────────────────────────
    val SurfaceBright get() = of(Color(0xFFFFFFFF), Color(0xFF2A2A2A))
    val SurfaceDim get() = of(Color(0xFFF4F8FB), Color(0xFF121212))
    val SurfaceContainer get() = of(Color(0xFFFBFBFB), Color(0xFF1E1E1E))
    val SurfaceContainerLow get() = of(Color(0xFFFFFFFF), Color(0xFF1A1A1A))
    val SurfaceContainerLowest get() = of(Color(0xFFFFFFFF), Color(0xFF121212))
    val SurfaceContainerHigh get() = of(Color(0xFFF2F9FF), Color(0xFF252525))
    val SurfaceContainerHighest get() = of(Color(0xFFEDF5FC), Color(0xFF303030))

    // ── Component-specific ───────────────────────────
    val NavInactive get() = of(Color(0xFFA8A8A8), Color(0xFF757575))
    val IconColor get() = of(Color(0xFF888888), Color(0xFFB0B0B0))
    val BackButtonBackground get() = of(Color(0x66FFFFFF), Color(0x33FFFFFF))

    // ── Semantic status (same in both themes) ────────
    val Success = Color(0xFF249F58)
    val Warning = Color(0xFFFFC107)
    val Info = Color(0xFF42A5F5)
    val StatusProcessing = Color(0xFF4E9FE0)
    val NavyBlue = Color(0xFF1A47B8)
    val Purple = Color(0xFF543592)
}
