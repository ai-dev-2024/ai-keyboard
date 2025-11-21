# Theming Guide

This guide explains how to customize themes and appearance in AI Keyboard.

## Table of Contents

- [Brand Colors](#brand-colors)
- [Theme Presets](#theme-presets)
- [Custom Themes](#custom-themes)
- [Keycap Design Rules](#keycap-design-rules)
- [Compose Theme Structure](#compose-theme-structure)
- [Adding New Themes](#adding-new-themes)

---

## Brand Colors

AI Keyboard uses a carefully designed color palette that emphasizes clarity, accessibility, and modern aesthetics.

### Primary Colors

#### Electric Blue
- **Hex**: `#3A7BFF`
- **RGB**: `58, 123, 255`
- **Usage**: Primary actions, app icon background, key accents
- **Light Theme**: Primary color
- **Dark Theme**: Lighter variant (`#7BA0FF`)

#### Deep Indigo
- **Hex**: `#1E2A55`
- **RGB**: `30, 42, 85`
- **Usage**: Dark theme backgrounds, on-primary text
- **Light Theme**: onPrimaryContainer
- **Dark Theme**: Primary background

### Secondary Colors

#### AI Teal
- **Hex**: `#00C7B7`
- **RGB**: `0, 199, 183`
- **Usage**: Secondary actions, accent elements, active states
- **Light/Dark**: Secondary color

#### Sky Mint
- **Hex**: `#A0FFE9`
- **RGB**: `160, 255, 233`
- **Usage**: Primary container, highlights, light accents
- **Light Theme**: primaryContainer
- **Dark Theme**: Accent elements

### Accent Colors

#### Magenta Pulse
- **Hex**: `#FF2BA3`
- **RGB**: `255, 43, 163`
- **Usage**: Recording indicator, highlights, error states

#### AI Gold
- **Hex**: `#F9D65C`
- **RGB**: `249, 214, 92`
- **Usage**: Premium features, Pro badge, special highlights

### Neutral Colors

#### Pure White
- **Hex**: `#FFFFFF`
- **Usage**: Light theme background, on-dark text

#### Soft Gray
- **Hex**: `#E8EAF0`
- **Usage**: Surface variants, dividers, inactive states

#### Dark Charcoal
- **Hex**: `#1D1D1F`
- **Usage**: Dark theme background, on-light text

### Color Restrictions

- ❌ **No purple and blue combinations** (per brand rules)
- ✅ Use brand colors consistently
- ✅ Ensure accessibility (WCAG AA contrast ratios)

---

## Theme Presets

AI Keyboard includes several theme presets that users can select:

### Default (Blue)
- **Primary**: Electric Blue (`#3A7BFF`)
- **Secondary**: AI Teal (`#00C7B7`)
- **Background**: Pure White / Dark Charcoal
- **Best For**: General use, professional appearance

### Dark Pro
- **Primary**: Lighter Electric Blue (`#7BA0FF`)
- **Secondary**: AI Teal (`#00C7B7`)
- **Background**: Dark Charcoal (`#1D1D1F`)
- **Best For**: Dark mode users, reduced eye strain

### Midnight Neon
- **Primary**: AI Teal (`#00C7B7`)
- **Secondary**: Magenta Pulse (`#FF2BA3`)
- **Background**: Dark Charcoal (`#1D1D1F`)
- **Best For**: Neon aesthetic, modern look

### Mint Glow
- **Primary**: AI Teal (`#00C7B7`)
- **Secondary**: Sky Mint (`#A0FFE9`)
- **Background**: Pure White / Light Gray
- **Best For**: Fresh, clean appearance

### Minimal White
- **Primary**: Deep Indigo (`#1E2A55`)
- **Secondary**: Soft Gray (`#E8EAF0`)
- **Background**: Pure White (`#FFFFFF`)
- **Best For**: Minimalist design, maximum readability

---

## Custom Themes

Users can create custom themes with their own color combinations:

### Creating a Custom Theme

1. **Open AI Keyboard Settings**
2. Navigate to **"Appearance"** → **"Themes"**
3. Tap **"Create Custom Theme"**
4. Choose colors for:
   - Primary color
   - Secondary color
   - Background color
   - Key color
   - Text color
5. Save the theme

### Custom Theme Options

- **Primary Color**: Main accent color
- **Secondary Color**: Secondary accent color
- **Background Color**: Keyboard background
- **Key Color**: Key background color
- **Text Color**: Key text color
- **Active Key Color**: Active/pressed key color

---

## Keycap Design Rules

### Keycap Shape

- **Shape**: Rounded rectangles
- **Corner Radius**: 8-12dp
- **Hit Target**: Minimum 48dp × 48dp
- **Spacing**: 4-8dp between keys

### Keycap Colors

- **Background**: Follows theme (light/dark)
- **Text Color**: High contrast on key background
- **Active State**: Slightly lighter/darker shade
- **Pressed State**: Visual feedback (scale, shadow)

### Keycap Typography

- **Font**: Inter Regular or system font
- **Size**: 14-16sp (adjustable)
- **Weight**: Regular
- **Style**: Uppercase for letter keys

### Keycap Spacing

- **Horizontal Spacing**: 4-8dp between keys
- **Vertical Spacing**: 4-8dp between rows
- **Padding**: 8-12dp inside keys

---

## Compose Theme Structure

AI Keyboard uses Jetpack Compose with Material 3 theme system.

### Theme Configuration

```kotlin
@Composable
fun AIKeyboardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themePreset: ThemePreset = ThemePreset.Default,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themePreset) {
        ThemePreset.Default -> if (darkTheme) DarkBlueTheme else LightBlueTheme
        ThemePreset.DarkPro -> DarkProTheme
        ThemePreset.MidnightNeon -> MidnightNeonTheme
        ThemePreset.MintGlow -> MintGlowTheme
        ThemePreset.MinimalWhite -> MinimalWhiteTheme
        ThemePreset.Custom -> customColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = InterTypography,
        content = content
    )
}
```

### Color Scheme Definition

```kotlin
val LightBlueTheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = PureWhite,
    primaryContainer = SkyMint,
    onPrimaryContainer = DeepIndigo,
    secondary = AITeal,
    onSecondary = PureWhite,
    tertiary = MagentaPulse,
    background = PureWhite,
    surface = PureWhite,
    onSurface = DeepIndigo,
    onSurfaceVariant = SoftGray
)

val DarkBlueTheme = darkColorScheme(
    primary = LighterElectricBlue,
    onPrimary = DeepIndigo,
    primaryContainer = DarkElectricBlue,
    onPrimaryContainer = SkyMint,
    secondary = AITeal,
    onSecondary = DarkCharcoal,
    tertiary = MagentaPulse,
    background = DarkCharcoal,
    surface = DarkCharcoal,
    onSurface = PureWhite,
    onSurfaceVariant = SoftGray
)
```

---

## Adding New Themes

### For Developers

To add a new theme preset:

1. **Define Color Scheme**

```kotlin
val NewTheme = lightColorScheme(
    primary = YourPrimaryColor,
    onPrimary = YourOnPrimaryColor,
    // ... other colors
)
```

2. **Add to ThemePreset Enum**

```kotlin
enum class ThemePreset {
    Default,
    DarkPro,
    MidnightNeon,
    MintGlow,
    MinimalWhite,
    NewTheme  // Add here
}
```

3. **Update Theme Function**

```kotlin
val colorScheme = when (themePreset) {
    // ... existing themes
    ThemePreset.NewTheme -> NewTheme
}
```

4. **Add Theme Display Name**

```kotlin
fun ThemePreset.displayName(): String {
    return when (this) {
        ThemePreset.NewTheme -> "New Theme"
        // ... other themes
    }
}
```

5. **Test Theme**

- Test in light mode
- Test in dark mode
- Verify accessibility (contrast ratios)
- Test on different devices

### For Contributors

If you'd like to contribute a new theme:

1. **Create Issue**: Create a GitHub issue describing your theme proposal
2. **Design Colors**: Choose colors that follow brand guidelines
3. **Implement**: Follow the steps above
4. **Test**: Test thoroughly on multiple devices
5. **Submit PR**: Submit a pull request with your theme

### Theme Guidelines

- ✅ **Follow Brand Colors**: Use brand colors when possible
- ✅ **Accessibility**: Ensure WCAG AA contrast ratios
- ✅ **Consistency**: Maintain visual consistency
- ❌ **No Purple/Blue**: Avoid purple and blue combinations
- ✅ **Test Thoroughly**: Test on multiple devices and themes

---

## Typography

### Font Families

#### Inter (Primary)
- **Headings**: Inter SemiBold / Bold
- **Body**: Inter Regular
- **Download**: [Google Fonts - Inter](https://fonts.google.com/specimen/Inter)

### Type Scale

| Style | Size | Weight | Usage |
|-------|------|--------|-------|
| Display Large | 57sp | Bold | Welcome screens |
| Headline Medium | 28sp | SemiBold | Card titles |
| Title Medium | 16sp | Medium | List items |
| Body Large | 16sp | Regular | Main content |
| Label Large | 14sp | Medium | Button labels |

---

## Accessibility

### High Contrast

- Detect system high contrast settings
- Increase color contrast ratios
- Thicker borders (4dp instead of 2dp)

### Color Blindness

- Use icons in addition to color
- Don't rely solely on color for state
- Test with color blindness simulators

### Large Font Support

- Support system font scale
- Minimum text size: 12sp
- Dynamic key sizing for very large fonts

---

## Resources

### Design Resources

- **Brand Colors**: See `branding/style-guide/UI_STYLE_GUIDE.md`
- **Logo Assets**: See `branding/logo/`
- **Typography**: Inter font from Google Fonts

### Implementation Resources

- **Material 3**: [Material Design 3](https://m3.material.io/)
- **Compose Theming**: [Jetpack Compose Theming](https://developer.android.com/jetpack/compose/themes)
- **Color System**: [Material 3 Color System](https://m3.material.io/styles/color/the-color-system/color-roles)

---

**Last Updated**: November 2024

**AI Keyboard** — Beautiful. Customizable. Yours.












