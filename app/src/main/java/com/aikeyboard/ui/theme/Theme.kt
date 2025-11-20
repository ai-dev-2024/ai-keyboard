package com.aikeyboard.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// AI Keyboard Brand Colors
val ElectricBlue = Color(0xFF3A7BFF)
val DeepIndigo = Color(0xFF1E2A55)
val AITeal = Color(0xFF00C7B7)
val SkyMint = Color(0xFFA0FFE9)
val MagentaPulse = Color(0xFFFF2BA3)
val AIGold = Color(0xFFF9D65C)
val PureWhite = Color(0xFFFFFFFF)
val SoftGray = Color(0xFFE8EAF0)
val DarkCharcoal = Color(0xFF1D1D1F)

// Theme Preset Data Class
data class ThemePreset(
    val name: String,
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme
)

// Default Theme (Blue)
private val DefaultLightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = PureWhite,
    primaryContainer = SkyMint,
    onPrimaryContainer = DeepIndigo,
    secondary = AITeal,
    onSecondary = PureWhite,
    secondaryContainer = SoftGray,
    onSecondaryContainer = DeepIndigo,
    tertiary = MagentaPulse,
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFFFE5F2),
    onTertiaryContainer = Color(0xFFB3005E),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = PureWhite,
    onErrorContainer = Color(0xFF410002),
    background = PureWhite,
    onBackground = DarkCharcoal,
    surface = PureWhite,
    onSurface = DarkCharcoal,
    surfaceVariant = SoftGray,
    onSurfaceVariant = Color(0xFF5A5F6B),
    outline = Color(0xFF8E939F),
    outlineVariant = Color(0xFFD0D3DA),
    scrim = Color(0xFF000000),
    inverseSurface = DarkCharcoal,
    inverseOnSurface = PureWhite,
    inversePrimary = Color(0xFF7BA0FF)
)

private val DefaultDarkColorScheme = darkColorScheme(
    primary = Color(0xFF7BA0FF), // Electric Blue (lighter for dark theme)
    onPrimary = DeepIndigo,
    primaryContainer = Color(0xFF0055CC),
    onPrimaryContainer = SkyMint,
    secondary = AITeal,
    onSecondary = DeepIndigo,
    secondaryContainer = Color(0xFF005049),
    onSecondaryContainer = SkyMint,
    tertiary = MagentaPulse,
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFF8F005F),
    onTertiaryContainer = Color(0xFFFFD6E9),
    error = Color(0xFFFFB4AB),
    errorContainer = Color(0xFF93000A),
    onError = Color(0xFF690005),
    onErrorContainer = Color(0xFFFFDAD6),
    background = DarkCharcoal,
    onBackground = SoftGray,
    surface = DarkCharcoal,
    onSurface = SoftGray,
    surfaceVariant = Color(0xFF5A5F6B),
    onSurfaceVariant = Color(0xFFD0D3DA),
    outline = Color(0xFF8E939F),
    outlineVariant = Color(0xFF3A3F4B),
    scrim = Color(0xFF000000),
    inverseSurface = SoftGray,
    inverseOnSurface = DarkCharcoal,
    inversePrimary = ElectricBlue
)

// Dark Pro Theme
private val DarkProLightColorScheme = lightColorScheme(
    primary = Color(0xFF1E2A55),
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFE8EAF0),
    onPrimaryContainer = Color(0xFF1E2A55),
    secondary = Color(0xFF00C7B7),
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFE0F7F5),
    onSecondaryContainer = Color(0xFF005049),
    tertiary = Color(0xFFFF2BA3),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFFFE5F2),
    onTertiaryContainer = Color(0xFFB3005E),
    background = PureWhite,
    onBackground = Color(0xFF1D1D1F),
    surface = PureWhite,
    onSurface = Color(0xFF1D1D1F),
    surfaceVariant = Color(0xFFE8EAF0),
    onSurfaceVariant = Color(0xFF5A5F6B),
    outline = Color(0xFF8E939F),
    outlineVariant = Color(0xFFD0D3DA)
)

private val DarkProDarkColorScheme = darkColorScheme(
    primary = Color(0xFF7BA0FF),
    onPrimary = Color(0xFF1E2A55),
    primaryContainer = Color(0xFF0055CC),
    onPrimaryContainer = Color(0xFFA0FFE9),
    secondary = Color(0xFF00C7B7),
    onSecondary = Color(0xFF1E2A55),
    secondaryContainer = Color(0xFF005049),
    onSecondaryContainer = Color(0xFFA0FFE9),
    tertiary = Color(0xFFFF2BA3),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFF8F005F),
    onTertiaryContainer = Color(0xFFFFD6E9),
    background = Color(0xFF0A0A0A),
    onBackground = Color(0xFFE8EAF0),
    surface = Color(0xFF0A0A0A),
    onSurface = Color(0xFFE8EAF0),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFD0D3DA),
    outline = Color(0xFF5A5F6B),
    outlineVariant = Color(0xFF3A3F4B)
)

// Midnight Neon Theme
private val MidnightNeonLightColorScheme = lightColorScheme(
    primary = Color(0xFF3A7BFF),
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFE0E8FF),
    onPrimaryContainer = Color(0xFF1E2A55),
    secondary = Color(0xFF00C7B7),
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFE0F7F5),
    onSecondaryContainer = Color(0xFF005049),
    tertiary = Color(0xFFFF2BA3),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFFFE5F2),
    onTertiaryContainer = Color(0xFFB3005E),
    background = Color(0xFF0A0A0F),
    onBackground = Color(0xFFE8EAF0),
    surface = Color(0xFF0A0A0F),
    onSurface = Color(0xFFE8EAF0),
    surfaceVariant = Color(0xFF1A1A2A),
    onSurfaceVariant = Color(0xFFD0D3DA)
)

private val MidnightNeonDarkColorScheme = darkColorScheme(
    primary = Color(0xFF7BA0FF),
    onPrimary = Color(0xFF1E2A55),
    primaryContainer = Color(0xFF0055CC),
    onPrimaryContainer = Color(0xFFA0FFE9),
    secondary = Color(0xFF00C7B7),
    onSecondary = Color(0xFF1E2A55),
    secondaryContainer = Color(0xFF005049),
    onSecondaryContainer = Color(0xFFA0FFE9),
    tertiary = Color(0xFFFF2BA3),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFF8F005F),
    onTertiaryContainer = Color(0xFFFFD6E9),
    background = Color(0xFF0A0A0F),
    onBackground = Color(0xFFE8EAF0),
    surface = Color(0xFF0A0A0F),
    onSurface = Color(0xFFE8EAF0),
    surfaceVariant = Color(0xFF1A1A2A),
    onSurfaceVariant = Color(0xFFD0D3DA),
    outline = Color(0xFF5A5F6B),
    outlineVariant = Color(0xFF3A3F4B)
)

// Mint Glow Theme
private val MintGlowLightColorScheme = lightColorScheme(
    primary = Color(0xFF00C7B7),
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFA0FFE9),
    onPrimaryContainer = Color(0xFF005049),
    secondary = Color(0xFF3A7BFF),
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFE0E8FF),
    onSecondaryContainer = Color(0xFF1E2A55),
    tertiary = Color(0xFFFF2BA3),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFFFE5F2),
    onTertiaryContainer = Color(0xFFB3005E),
    background = PureWhite,
    onBackground = Color(0xFF1D1D1F),
    surface = PureWhite,
    onSurface = Color(0xFF1D1D1F),
    surfaceVariant = Color(0xFFE8EAF0),
    onSurfaceVariant = Color(0xFF5A5F6B)
)

private val MintGlowDarkColorScheme = darkColorScheme(
    primary = Color(0xFF00C7B7),
    onPrimary = Color(0xFF005049),
    primaryContainer = Color(0xFF005049),
    onPrimaryContainer = Color(0xFFA0FFE9),
    secondary = Color(0xFF7BA0FF),
    onSecondary = Color(0xFF1E2A55),
    secondaryContainer = Color(0xFF0055CC),
    onSecondaryContainer = Color(0xFFA0FFE9),
    tertiary = Color(0xFFFF2BA3),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFF8F005F),
    onTertiaryContainer = Color(0xFFFFD6E9),
    background = Color(0xFF0F1A1A),
    onBackground = Color(0xFFE8EAF0),
    surface = Color(0xFF0F1A1A),
    onSurface = Color(0xFFE8EAF0),
    surfaceVariant = Color(0xFF1F2A2A),
    onSurfaceVariant = Color(0xFFD0D3DA)
)

// Minimal White Theme
private val MinimalWhiteLightColorScheme = lightColorScheme(
    primary = Color(0xFF1D1D1F),
    onPrimary = PureWhite,
    primaryContainer = Color(0xFFE8EAF0),
    onPrimaryContainer = Color(0xFF1D1D1F),
    secondary = Color(0xFF5A5F6B),
    onSecondary = PureWhite,
    secondaryContainer = Color(0xFFE8EAF0),
    onSecondaryContainer = Color(0xFF1D1D1F),
    tertiary = Color(0xFF3A7BFF),
    onTertiary = PureWhite,
    tertiaryContainer = Color(0xFFE0E8FF),
    onTertiaryContainer = Color(0xFF1E2A55),
    background = PureWhite,
    onBackground = Color(0xFF1D1D1F),
    surface = PureWhite,
    onSurface = Color(0xFF1D1D1F),
    surfaceVariant = Color(0xFFE8EAF0),
    onSurfaceVariant = Color(0xFF5A5F6B)
)

private val MinimalWhiteDarkColorScheme = darkColorScheme(
    primary = Color(0xFFE8EAF0),
    onPrimary = Color(0xFF1D1D1F),
    primaryContainer = Color(0xFF3A3F4B),
    onPrimaryContainer = Color(0xFFE8EAF0),
    secondary = Color(0xFFD0D3DA),
    onSecondary = Color(0xFF1D1D1F),
    secondaryContainer = Color(0xFF3A3F4B),
    onSecondaryContainer = Color(0xFFE8EAF0),
    tertiary = Color(0xFF7BA0FF),
    onTertiary = Color(0xFF1E2A55),
    tertiaryContainer = Color(0xFF0055CC),
    onTertiaryContainer = Color(0xFFA0FFE9),
    background = Color(0xFF1D1D1F),
    onBackground = Color(0xFFE8EAF0),
    surface = Color(0xFF1D1D1F),
    onSurface = Color(0xFFE8EAF0),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFD0D3DA)
)

// Theme Presets
val ThemePresets = mapOf(
    "default" to ThemePreset(
        name = "Default (Blue)",
        lightColorScheme = DefaultLightColorScheme,
        darkColorScheme = DefaultDarkColorScheme
    ),
    "dark_pro" to ThemePreset(
        name = "Dark Pro",
        lightColorScheme = DarkProLightColorScheme,
        darkColorScheme = DarkProDarkColorScheme
    ),
    "midnight_neon" to ThemePreset(
        name = "Midnight Neon",
        lightColorScheme = MidnightNeonLightColorScheme,
        darkColorScheme = MidnightNeonDarkColorScheme
    ),
    "mint_glow" to ThemePreset(
        name = "Mint Glow",
        lightColorScheme = MintGlowLightColorScheme,
        darkColorScheme = MintGlowDarkColorScheme
    ),
    "minimal_white" to ThemePreset(
        name = "Minimal White",
        lightColorScheme = MinimalWhiteLightColorScheme,
        darkColorScheme = MinimalWhiteDarkColorScheme
    )
)

// Shape System - 8dp key corners
private val KeyShape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
private val KeyboardShapes = Shapes(
    extraSmall = KeyShape,
    small = KeyShape,
    medium = KeyShape,
    large = KeyShape,
    extraLarge = KeyShape
)

// Animation Specs
val StandardAnimationSpec = tween<Float>(
    durationMillis = 200,
    easing = FastOutSlowInEasing
)

val FastAnimationSpec = tween<Float>(
    durationMillis = 150,
    easing = FastOutSlowInEasing
)

val SlowAnimationSpec = tween<Float>(
    durationMillis = 300,
    easing = FastOutSlowInEasing
)

// Local Theme Preset
val LocalThemePreset = compositionLocalOf { "default" }

@Composable
fun AIKeyboardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disable dynamic color to use brand colors
    themePreset: String = "default",
    content: @Composable () -> Unit
) {
    val preset = ThemePresets[themePreset] ?: ThemePresets["default"]!!
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> preset.darkColorScheme
        else -> preset.lightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalThemePreset provides themePreset) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = KeyboardShapes,
            content = content
        )
    }
}

