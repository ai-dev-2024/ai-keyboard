# AI Keyboard — UI Style Guide

**Version 1.0** | Last Updated: 2024

---

## Table of Contents

1. [Brand Overview](#brand-overview)
2. [Color System](#color-system)
3. [Typography](#typography)
4. [Logo & Icons](#logo--icons)
5. [UI Components](#ui-components)
6. [Layout Guidelines](#layout-guidelines)
7. [Animations](#animations)
8. [Accessibility](#accessibility)
9. [Implementation Examples](#implementation-examples)

---

## Brand Overview

**AI Keyboard** is a modern, privacy-first Android keyboard that combines intelligent typing with AI-driven offline voice recognition.

### Brand Personality
- **Futuristic but friendly**
- **Minimalistic but premium**
- **Clean, intuitive, and tech-forward**
- **Trustworthy (privacy-first / local processing)**

### Tone
Smart • Lightweight • Professional • Human-friendly

---

## Color System

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

### Neutrals

#### Pure White
- **Hex**: `#FFFFFF`
- **Usage**: Light theme background, on-dark text

#### Soft Gray
- **Hex**: `#E8EAF0`
- **Usage**: Surface variants, dividers, inactive states

#### Dark Charcoal
- **Hex**: `#1D1D1F`
- **Usage**: Dark theme background, on-light text

### Accents

#### Magenta Pulse
- **Hex**: `#FF2BA3`
- **RGB**: `255, 43, 163`
- **Usage**: Recording indicator, highlights, error states (when needed)

#### AI Gold
- **Hex**: `#F9D65C`
- **RGB**: `249, 214, 92`
- **Usage**: Premium features, Pro badge, special highlights

### Material 3 Color Mapping

```kotlin
// Light Theme
primary = ElectricBlue (#3A7BFF)
onPrimary = PureWhite (#FFFFFF)
primaryContainer = SkyMint (#A0FFE9)
onPrimaryContainer = DeepIndigo (#1E2A55)
secondary = AITeal (#00C7B7)
tertiary = MagentaPulse (#FF2BA3)
background = PureWhite (#FFFFFF)
surface = PureWhite (#FFFFFF)

// Dark Theme
primary = LighterElectricBlue (#7BA0FF)
onPrimary = DeepIndigo (#1E2A55)
primaryContainer = DarkElectricBlue (#0055CC)
onPrimaryContainer = SkyMint (#A0FFE9)
secondary = AITeal (#00C7B7)
tertiary = MagentaPulse (#FF2BA3)
background = DarkCharcoal (#1D1D1F)
surface = DarkCharcoal (#1D1D1F)
```

---

## Typography

### Font Families

#### Inter (Primary)
- **Headings**: Inter SemiBold / Bold
- **Body**: Inter Regular
- **Download**: [Google Fonts - Inter](https://fonts.google.com/specimen/Inter)
- **Fallback**: System Sans-Serif (Roboto on Android)

#### JetBrains Mono (Code/Metrics)
- **Usage**: Debug screens, code snippets, technical metrics
- **Download**: [JetBrains Mono](https://www.jetbrains.com/lp/mono/)
- **Fallback**: System Monospace

### Type Scale (Material 3 Compliant)

| Style | Size | Weight | Line Height | Letter Spacing | Usage |
|-------|------|--------|-------------|----------------|-------|
| **Display Large** | 57sp | Bold | 64sp | -0.25sp | Welcome screens |
| **Display Medium** | 45sp | Bold | 52sp | 0sp | Major headings |
| **Display Small** | 36sp | SemiBold | 44sp | 0sp | Section headers |
| **Headline Large** | 32sp | SemiBold | 40sp | 0sp | Page titles |
| **Headline Medium** | 28sp | SemiBold | 36sp | 0sp | Card titles |
| **Headline Small** | 24sp | SemiBold | 32sp | 0sp | Subsection headers |
| **Title Large** | 22sp | SemiBold | 28sp | 0sp | Dialog titles |
| **Title Medium** | 16sp | Medium | 24sp | 0.15sp | List items |
| **Title Small** | 14sp | Medium | 20sp | 0.1sp | Buttons |
| **Body Large** | 16sp | Regular | 24sp | 0.5sp | Main content |
| **Body Medium** | 14sp | Regular | 20sp | 0.25sp | Secondary content |
| **Body Small** | 12sp | Regular | 16sp | 0.4sp | Captions |
| **Label Large** | 14sp | Medium | 20sp | 0.1sp | Button labels |
| **Label Medium** | 12sp | Medium | 16sp | 0.5sp | Tags |
| **Label Small** | 11sp | Medium | 16sp | 0.5sp | Tiny labels |

---

## Logo & Icons

### Logo Concepts

Four logo concepts are available, each in three variants:

#### Concept A: AI Wave Key
- **Solid**: `branding/logo/svg/concept-a-ai-wave-key-solid.svg`
- **Outline**: `branding/logo/svg/concept-a-ai-wave-key-outline.svg`
- **Gradient**: `branding/logo/svg/concept-a-ai-wave-key-gradient.svg`
- **Description**: Keyboard key with "AI" monogram formed by wave/speech pattern
- **Recommended**: Best balance of recognition and simplicity

#### Concept B: Spark Key
- **Solid**: `branding/logo/svg/concept-b-spark-key-solid.svg`
- **Outline**: `branding/logo/svg/concept-b-spark-key-outline.svg`
- **Gradient**: `branding/logo/svg/concept-b-spark-key-gradient.svg`
- **Description**: Keycap with spark/star at corner representing AI action
- **Recommended**: Most minimal, app-store friendly

#### Concept C: Neural Keyboard
- **Solid**: `branding/logo/svg/concept-c-neural-keyboard-solid.svg`
- **Outline**: `branding/logo/svg/concept-c-neural-keyboard-outline.svg`
- **Gradient**: `branding/logo/svg/concept-c-neural-keyboard-gradient.svg`
- **Description**: Keyboard key with neural network nodes
- **Recommended**: Tech-forward, represents AI power

#### Concept D: Mic + Key Hybrid
- **Solid**: `branding/logo/svg/concept-d-mic-key-hybrid-solid.svg`
- **Outline**: `branding/logo/svg/concept-d-mic-key-hybrid-outline.svg`
- **Gradient**: `branding/logo/svg/concept-d-mic-key-hybrid-gradient.svg`
- **Description**: Microphone merging into keycap, represents voice typing
- **Recommended**: Emphasizes voice input USP

### Adaptive Icons

Android 13+ adaptive icons located in:
- **Concept A (Default)**: `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- **Concept B**: `branding/adaptive-icons/ic_launcher_spark.xml`
- **Concept C**: `branding/adaptive-icons/ic_launcher_neural.xml`
- **Concept D**: `branding/adaptive-icons/ic_launcher_mic.xml`

### Icon Guidelines

- **Style**: Material 3 compliant
- **Corner Radius**: 2.0dp
- **Stroke Width**: 2.0dp (for outlined icons)
- **Fill**: Solid or Round variants
- **Colors**: Use brand colors (Electric Blue, AI Teal, Sky Mint)
- **Avoid**: Sharp angles, purple/blue gradients (per brand rules)

### Icon Types

1. **Mic Icon**: Voice input button
2. **Sound Wave Icon**: Audio/voice indicator
3. **Keyboard Icon**: Keyboard settings
4. **Spark Icon**: AI features
5. **AI Chip Icon**: Model selection

---

## UI Components

### Keyboard Layout

#### Key Design
- **Shape**: Rounded rectangles
- **Corner Radius**: 8-12dp
- **Hit Target**: Minimum 48dp × 48dp
- **Spacing**: 4-8dp between keys
- **Background**: Follows theme (light/dark)
- **Text Color**: High contrast on key background

#### QWERTY Layout
- Follow standard Android keyboard proportions
- Bottom row: Space bar (40% width), function keys
- Optional floating keyboard mode
- Support for gesture typing

#### Swipe Trail
- **Color**: AI Teal (`#00C7B7`) with 40% opacity
- **Width**: 4-6dp
- **Style**: Smooth curve following finger movement
- **Animation**: Fade out after release (150-200ms)

### Voice Input UI

#### Recording Indicator
- **Color**: Magenta Pulse (`#FF2BA3`) with glow effect
- **Animation**: Pulsing scale animation (1.0 → 1.1 → 1.0)
- **Duration**: Continuous while recording
- **Haptic**: Vibration on press/release

#### Waveform Display
- **Style**: Large, readable waveform
- **Color**: AI Teal (`#00C7B7`) on dark background, Deep Indigo on light
- **Height**: 60-80dp
- **Update Rate**: Real-time audio visualization

#### Suggestion Bar
- **Background**: Primary container color
- **Text**: Primary color (Electric Blue)
- **Animation**: Crossfade for text updates (200ms)
- **Placement**: Above keyboard

#### Final Text Insertion
- **Animation**: Fade in (300ms) + slide up (8dp)
- **Style**: Smooth, non-jarring

### Model Selection UI

#### Card Layout
- **Style**: Material 3 Cards
- **Elevation**: 1-2dp
- **Padding**: 16dp
- **Spacing**: 12dp between cards

#### Card Content
- **Model Name**: Title Large (22sp, SemiBold)
- **Size/RAM/Languages**: Body Medium (14sp)
- **Active Indicator**: Teal or Gold border (4dp)
- **Recommended Badge**: Gold background with "Recommended" text

#### Selection States
- **Active**: Teal border (`#00C7B7`) or Gold (`#F9D65C`)
- **Inactive**: Default card styling
- **Pressed**: Scale 0.98 (150ms)

### Settings UI

#### Section Organization
1. **Appearance**
   - Theme selection (Light/Dark/System)
   - Keyboard height adjustment
   - Key style preferences

2. **Typing**
   - Auto-correct toggle
   - Gesture typing toggle
   - Sound on keypress
   - Haptic feedback

3. **Clipboard**
   - Clipboard manager
   - History settings

4. **Emoji/GIF**
   - Emoji preferences
   - GIF integration

5. **AI Voice Input**
   - Model selection
   - Download/delete models
   - Recording sensitivity

6. **About**
   - App version
   - Privacy policy
   - Open source licenses

7. **Support**
   - Ko-fi link (❤️ Support the Developer)

#### Preference Items
- **Switch**: Material 3 Switch component
- **Spacing**: 8dp vertical between items
- **Dividers**: Between sections only
- **Icons**: 24dp, leading edge aligned

---

## Layout Guidelines

### Spacing System

| Token | Value | Usage |
|-------|-------|-------|
| **xs** | 4dp | Minimal spacing, tight groups |
| **sm** | 8dp | Between related items |
| **md** | 16dp | Standard padding, card padding |
| **lg** | 24dp | Section spacing |
| **xl** | 32dp | Large gaps, screen margins |
| **xxl** | 48dp | Major sections |

### Screen Layouts

#### Settings Screen
- **Top Padding**: 16dp
- **Side Padding**: 16dp
- **Section Spacing**: 24dp
- **Item Spacing**: 8dp

#### Keyboard Screen
- **Key Padding**: 4dp
- **Keyboard Margin**: 0dp (full width)
- **Bottom Padding**: 8dp (safe area)

#### Model Selection
- **Card Margin**: 16dp (sides), 12dp (top/bottom)
- **Card Padding**: 16dp
- **Content Spacing**: 12dp

---

## Animations

### Timing Standards

- **Fast**: 150ms (immediate feedback)
- **Standard**: 200ms (default transitions)
- **Slow**: 300ms (complex animations)

### Easing Curves

- **Standard**: `FastOutSlowInEasing`
- **Enter/Exit**: `FastOutLinearInEasing` / `LinearOutSlowInEasing`
- **Bounce**: Use sparingly, only for playful elements

### Animation Types

#### 1. Crossfade (Suggestions)
```kotlin
Crossfade(
    targetState = suggestionText,
    animationSpec = tween(200),
    label = "suggestion_crossfade"
)
```

#### 2. Scale-in (Model Cards)
```kotlin
scaleIn(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
)
```

#### 3. Ripple (Material 3)
- Default Material 3 ripple effect
- Color: Primary color with opacity

#### 4. Pulse (Recording Indicator)
```kotlin
infiniteRepeatable(
    animation = tween(1000, easing = LinearEasing),
    repeatMode = RepeatMode.Reverse
)
```

---

## Accessibility

### High Contrast Mode
- Detect system high contrast settings
- Increase color contrast ratios
- Thicker borders (4dp instead of 2dp)

### Large Font Support
- Support system font scale
- Minimum text size: 12sp
- Dynamic key sizing for very large fonts

### Adjustable Key Height
- Range: 36dp - 64dp
- Default: 48dp
- Persist user preference

### Haptic Feedback
- **Mic Press/Release**: Strong haptic (150ms)
- **Key Press**: Medium haptic (50ms)
- **Error**: Double haptic pattern
- **Settings**: Respect system haptic preferences

### Screen Reader Support
- **Content Descriptions**: All interactive elements
- **Keyboard**: Announce key presses (optional)
- **Voice Input**: Announce recording state

### Color Blindness
- Use icons in addition to color
- Don't rely solely on color for state
- Test with color blindness simulators

---

## Implementation Examples

### Using Brand Colors in Compose

```kotlin
import com.aikeyboard.ui.theme.*

// Using theme colors
Surface(
    color = MaterialTheme.colorScheme.primary,
    content = { /* ... */ }
)

// Using direct brand colors (when needed)
Box(
    modifier = Modifier
        .background(ElectricBlue)
        .padding(16.dp)
)
```

### Typography Usage

```kotlin
// Headings
Text(
    text = "AI Keyboard",
    style = MaterialTheme.typography.headlineMedium
)

// Body text
Text(
    text = "The Offline AI Voice Keyboard",
    style = MaterialTheme.typography.bodyLarge
)

// Code/Metrics (when needed)
Text(
    text = "Model: Parakeet 0.6B",
    style = CodeTypography
)
```

### Recording Indicator

```kotlin
@Composable
fun RecordingIndicator(isRecording: Boolean) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = if (isRecording) MagentaPulse else ElectricBlue,
                shape = CircleShape
            )
            .scale(
                animateFloatAsState(
                    targetValue = if (isRecording) 1.1f else 1.0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                ).value
            )
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice Input",
            tint = PureWhite
        )
    }
}
```

### Model Selection Card

```kotlin
@Composable
fun ModelCard(
    model: Model,
    isActive: Boolean,
    isRecommended: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(
                width = if (isActive) 4.dp else 0.dp,
                color = if (isRecommended) AIGold else AITeal,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = model.name,
                    style = MaterialTheme.typography.titleLarge
                )
                if (isRecommended) {
                    Surface(
                        color = AIGold,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Recommended",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepIndigo
                        )
                    }
                }
            }
            Text(
                text = "Size: ${model.size} • RAM: ${model.ram}GB",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Languages: ${model.languages.joinToString()}"
            )
        }
    }
}
```

---

## Asset Organization

```
app/src/main/res/
├── mipmap-anydpi-v26/      # Adaptive icons
├── drawable/               # Vector drawables, icons
├── values/
│   ├── colors.xml         # Color tokens
│   └── themes.xml         # Theme definitions
└── font/                   # Custom fonts (Inter, JetBrains Mono)

branding/
├── logo/
│   ├── svg/               # All logo SVG variants
│   └── png/               # PNG exports (various sizes)
├── adaptive-icons/        # Alternative adaptive icon concepts
├── colors/                # Color palette exports
├── typography/            # Typography specs
├── style-guide/           # This guide
└── screenshots/           # Mockup documentation
```

---

## Brand Compliance Checklist

- [ ] No purple/blue color combinations in design
- [ ] All animations are subtle (150-300ms)
- [ ] Typography uses Inter (or system fallback)
- [ ] Icons follow Material 3 guidelines
- [ ] Colors match brand palette
- [ ] Accessibility features implemented
- [ ] Dark mode properly supported
- [ ] Logo used consistently across screens

---

## Resources

- **Inter Font**: https://fonts.google.com/specimen/Inter
- **JetBrains Mono**: https://www.jetbrains.com/lp/mono/
- **Material 3 Design**: https://m3.material.io/
- **Material 3 Color System**: https://m3.material.io/styles/color/the-color-system/color-roles
- **Android Adaptive Icons**: https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive

---

**End of Style Guide**

For questions or updates, please refer to the project maintainer.












