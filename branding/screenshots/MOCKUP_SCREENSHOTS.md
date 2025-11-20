# AI Keyboard — Screen Mockup Documentation

**Version 1.0** | Last Updated: 2024

This document describes the UI mockups and screenshots that showcase the AI Keyboard branding, themes, and voice input experience.

---

## Overview

The following mockups demonstrate:

1. **Light Theme Keyboard Interface**
2. **Dark Theme Keyboard Interface**
3. **Voice Input Recording State**
4. **Model Selection Screen**
5. **Settings Screen**

---

## Screen 1: Light Theme Keyboard

### Description
Standard QWERTY keyboard in light theme, showing the main typing interface.

### Key Elements

#### Keyboard Layout
- **Background**: Pure White (`#FFFFFF`)
- **Key Background**: Soft Gray (`#E8EAF0`)
- **Key Text**: Dark Charcoal (`#1D1D1F`)
- **Key Shape**: Rounded rectangles (12dp corner radius)
- **Key Spacing**: 4dp between keys
- **Special Keys**: 
  - Space bar (Electric Blue accent, 40% width)
  - Mic button (Electric Blue background, white mic icon)
  - Enter/Shift (Electric Blue text)

#### Top Bar
- **Background**: Sky Mint (`#A0FFE9`)
- **Suggestion Text**: Deep Indigo (`#1E2A55`)
- **Text Style**: Body Large (16sp, Inter Regular)
- **Padding**: 12dp horizontal, 8dp vertical

#### Visual Specifications
- **Screen Width**: 1080dp
- **Keyboard Height**: 280dp (adjustable 36-64dp key height)
- **Key Height**: 48dp (default)
- **Font**: Inter Regular, 14sp on keys

### Design Notes
- Clean, minimal appearance
- High contrast for readability
- Subtle shadows on keys (1dp elevation)
- Smooth rounded corners throughout

---

## Screen 2: Dark Theme Keyboard

### Description
Same keyboard layout in dark theme for night/low-light usage.

### Key Elements

#### Keyboard Layout
- **Background**: Dark Charcoal (`#1D1D1F`)
- **Key Background**: Surface Variant (`#5A5F6B`)
- **Key Text**: Soft Gray (`#E8EAF0`)
- **Key Shape**: Same rounded rectangles
- **Special Keys**:
  - Space bar (Lighter Electric Blue `#7BA0FF` accent)
  - Mic button (Electric Blue background)
  - Enter/Shift (Electric Blue text)

#### Top Bar
- **Background**: Primary Container Dark (`#0055CC`)
- **Suggestion Text**: Sky Mint (`#A0FFE9`)
- **Text Style**: Same as light theme

### Design Notes
- Maintains readability in dark conditions
- Reduced eye strain
- Consistent spacing and sizing with light theme
- Smooth theme transitions (200ms fade)

---

## Screen 3: Voice Input Recording State

### Description
Keyboard interface during active voice recording, showcasing the recording indicator and waveform.

### Key Elements

#### Recording Indicator
- **Position**: Center of keyboard (temporary overlay)
- **Size**: 120dp × 120dp
- **Background**: Magenta Pulse (`#FF2BA3`) with glow effect
- **Shape**: Circle
- **Icon**: White microphone icon (48dp)
- **Animation**: Pulsing scale (1.0 → 1.1 → 1.0, 1000ms loop)
- **Haptic**: Strong vibration on press/release

#### Waveform Display
- **Position**: Above keyboard, replacing suggestion bar
- **Height**: 80dp
- **Width**: Full width
- **Color**: AI Teal (`#00C7B7`) on dark background
- **Style**: Smooth animated waveform
- **Update Rate**: Real-time (60fps)

#### Partial Text Display
- **Position**: Below waveform (if available)
- **Text**: Partial transcription in suggestion bar style
- **Color**: Primary color (Electric Blue / lighter variant)
- **Animation**: Crossfade updates (200ms)

#### Keyboard State
- **Keys**: Dimmed (60% opacity) while recording
- **Interaction**: Disabled during recording
- **Background**: Slightly darkened overlay (20% black overlay)

### Design Notes
- Clear visual feedback that recording is active
- Non-intrusive but prominent indicator
- Smooth animations prevent jarring transitions
- Accessibility: Haptic + visual + audio (optional) feedback

---

## Screen 4: Model Selection Screen

### Description
Settings screen showing available AI voice models with card-based layout.

### Key Elements

#### Screen Header
- **Title**: "AI Voice Input Models"
- **Style**: Headline Medium (28sp, Inter SemiBold)
- **Color**: Primary color
- **Padding**: 24dp top, 16dp horizontal

#### Model Cards

**Card 1: Parakeet 0.6B (Active & Recommended)**
- **Background**: White (light) / Dark Charcoal (dark)
- **Border**: Gold (`#F9D65C`), 4dp width
- **Elevation**: 2dp
- **Padding**: 16dp
- **Content**:
  - **Title**: "Parakeet 0.6B" (22sp, SemiBold)
  - **Badge**: "Recommended" (Gold background, Deep Indigo text)
  - **Size**: "Model Size: 240 MB" (14sp, Regular)
  - **RAM**: "Min RAM: 2 GB" (14sp, Regular)
  - **Languages**: "Languages: English, Spanish, French" (14sp, Regular)
  - **Status**: "✓ Active" (AI Teal color)

**Card 2: Whisper Small (Inactive)**
- **Background**: White (light) / Dark Charcoal (dark)
- **Border**: None (default card styling)
- **Elevation**: 1dp
- **Content**: Similar structure without "Active" indicator
- **Download Button**: "Download" (Text button, Electric Blue)

#### Card Spacing
- **Between Cards**: 12dp vertical
- **Side Margins**: 16dp
- **Animation**: Scale-in on appear (200ms spring)

### Design Notes
- Clear visual hierarchy
- Active model clearly distinguished
- Recommended badge draws attention appropriately
- Touch targets meet accessibility standards (48dp minimum)

---

## Screen 5: Settings Screen

### Description
Main settings screen showing organized sections with Material 3 components.

### Key Elements

#### Screen Layout
- **Background**: Pure White (light) / Dark Charcoal (dark)
- **Padding**: 16dp sides, 24dp top
- **Scrollable**: Vertical scrolling for long content

#### Section 1: Appearance
- **Section Header**: "Appearance" (Headline Small, 24sp)
- **Items**:
  - Theme: "Theme" → "System Default" (Text with trailing chevron)
  - Keyboard Height: Slider (36-64dp range, default 48dp)
  - Key Style: "Material 3" (Text, read-only for now)

#### Section 2: Typing
- **Section Header**: "Typing"
- **Items**:
  - Auto-correct: Switch (enabled)
  - Gesture Typing: Switch (enabled)
  - Sound on Keypress: Switch (disabled)
  - Haptic Feedback: Switch (enabled)

#### Section 3: AI Voice Input
- **Section Header**: "AI Voice Input"
- **Items**:
  - Voice Input Enabled: Switch (enabled)
  - Active Model: "Parakeet 0.6B" (Navigation to model selection)
  - Model Manager: "Manage Models" (Navigation)

#### Section 4: About
- **Section Header**: "About"
- **Items**:
  - App Version: "Version 1.0.0" (Text, non-interactive)
  - Privacy Policy: Link (opens in browser)
  - Open Source Licenses: Navigation

#### Section 5: Support
- **Section Header**: "Support"
- **Items**:
  - Ko-fi Link: "Support AI Keyboard ❤️" (Link, opens Ko-fi)
    - Icon: Heart emoji or icon
    - Color: Magenta Pulse or Electric Blue

#### Dividers
- **Style**: 1dp horizontal divider
- **Color**: Outline variant
- **Placement**: Between sections only (not between items)

### Design Notes
- Logical grouping of related settings
- Clear navigation hierarchy
- Consistent spacing (8dp between items, 24dp between sections)
- Material 3 components throughout
- Support link prominently placed

---

## Animation Sequences

### Theme Transition
1. **Trigger**: User switches theme in settings
2. **Duration**: 200ms
3. **Effect**: Crossfade between light/dark color schemes
4. **Implementation**: `Crossfade` composable with `tween(200)`

### Recording Start
1. **Trigger**: User presses mic button
2. **Sequence**:
   - Mic button scales to 1.1 (100ms)
   - Recording indicator fades in from center (150ms)
   - Waveform slides up from suggestion bar (200ms)
   - Keys dim to 60% opacity (150ms)
   - Haptic feedback (strong, 150ms)
3. **Total Duration**: ~300ms

### Recording End
1. **Trigger**: User releases mic button or recording completes
2. **Sequence**:
   - Recording indicator fades out (200ms)
   - Waveform slides down (200ms)
   - Partial text crossfades to final text (200ms)
   - Final text fades in + slides up (300ms)
   - Keys return to full opacity (150ms)
   - Haptic feedback (strong, 150ms)
3. **Total Duration**: ~400ms

### Model Card Selection
1. **Trigger**: User taps inactive model card
2. **Sequence**:
   - Card scales to 0.98 (100ms)
   - New active border appears (150ms)
   - Previous active card border fades out (150ms)
   - Card returns to scale 1.0 (100ms)
3. **Total Duration**: ~200ms

---

## Color Usage Summary

### Light Theme
- **Backgrounds**: Pure White (`#FFFFFF`)
- **Surfaces**: Pure White or Soft Gray (`#E8EAF0`)
- **Primary**: Electric Blue (`#3A7BFF`)
- **Text**: Dark Charcoal (`#1D1D1F`)
- **Accents**: AI Teal (`#00C7B7`), Sky Mint (`#A0FFE9`)

### Dark Theme
- **Backgrounds**: Dark Charcoal (`#1D1D1F`)
- **Surfaces**: Dark Charcoal or Surface Variant (`#5A5F6B`)
- **Primary**: Lighter Electric Blue (`#7BA0FF`)
- **Text**: Soft Gray (`#E8EAF0`)
- **Accents**: AI Teal (`#00C7B7`), Sky Mint (`#A0FFE9`)

### Special States
- **Recording**: Magenta Pulse (`#FF2BA3`)
- **Active/Selected**: AI Teal (`#00C7B7`) or AI Gold (`#F9D65C`)
- **Error**: Material 3 error color (red)

---

## Implementation Notes

### Screenshots/Mockups Creation
To create actual mockups:

1. **Design Tools**: Figma, Adobe XD, or Sketch
2. **Template**: Use Android phone frame template (1080 × 2340dp, 9:19.5)
3. **Assets**: Use logo SVGs, brand colors, Inter font
4. **Export**: PNG at 2x resolution for screenshots (2160 × 4680px)

### Testing Checklist
- [ ] All screens render correctly in light theme
- [ ] All screens render correctly in dark theme
- [ ] Animations are smooth (60fps)
- [ ] Text is readable at all sizes
- [ ] Touch targets meet 48dp minimum
- [ ] Colors have sufficient contrast (WCAG AA)
- [ ] Icons are clear at all sizes
- [ ] Recording state is clearly visible

---

## Next Steps

1. Create actual mockup images using design tools
2. Export screenshots in multiple resolutions:
   - 1080 × 2340px (standard)
   - 1440 × 3120px (high-res)
   - 720 × 1560px (thumbnail)
3. Add to Play Store listing
4. Include in app onboarding
5. Use for marketing materials

---

**End of Mockup Documentation**

