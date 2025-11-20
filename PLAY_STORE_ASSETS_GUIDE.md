# AI Keyboard — Play Store Assets Guide

## Overview

This guide outlines all visual assets required for Play Store listing and provides specifications for creating them.

## Required Assets

### 1. App Icon (512×512)
**Format:** PNG  
**Size:** 512×512 pixels  
**Background:** Transparent or solid color  
**File:** `app-icon-512.png`

**Requirements:**
- High resolution, sharp edges
- No text (icon should be recognizable without text)
- Follows Material Design guidelines
- Works on both light and dark backgrounds
- Can be generated from existing adaptive icon

**Creation:**
```bash
# Export from adaptive icon resources
# Use app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml
# Or use branding assets in branding/logo/
```

---

### 2. Feature Graphic (1024×500)
**Format:** PNG or JPG  
**Size:** 1024×500 pixels  
**File:** `feature-graphic-1024x500.png`

**Requirements:**
- Horizontal banner for Play Store listing
- Should include:
  - App name: "AI Keyboard"
  - Tagline: "Offline AI Voice Typing"
  - Key features (3-4 bullet points)
  - Visual elements (keyboard, microphone, AI icon)
- Text should be readable at small sizes
- Follow brand colors (Electric Blue, AI Teal)
- No purple/blue combinations (per brand rules)

**Design Elements:**
- Background: Gradient (Electric Blue to AI Teal)
- App icon/logo (centered or left)
- Feature highlights:
  - "100% Offline Processing"
  - "AI-Powered Voice Typing"
  - "Complete Privacy"
  - "Fully Customizable"

**Template Location:**
- Use mockup descriptions in `branding/screenshots/MOCKUP_SCREENSHOTS.md`
- Reference `branding/style-guide/UI_STYLE_GUIDE.md` for colors

---

### 3. Screenshots (1080×1920)
**Format:** PNG or JPG  
**Size:** 1080×1920 pixels (9:16 aspect ratio)  
**Quantity:** 7 screenshots required  
**Files:** `screenshot-01.png` through `screenshot-07.png`

**Screenshot List:**

#### Screenshot 1: Main Keyboard (Light Theme)
- Show keyboard in light theme
- Display typing interface
- Highlight clean, modern design
- Include emoji picker or suggestions visible

#### Screenshot 2: Main Keyboard (Dark Theme)
- Show keyboard in dark theme
- Demonstrate theme switching
- Show consistency across themes

#### Screenshot 3: Voice Input
- Show voice input interface
- Display microphone button
- Show "Recording..." or transcription state
- Highlight offline processing

#### Screenshot 4: Model Selection
- Show model manager screen
- Display available models
- Show model installation options
- Highlight flexibility

#### Screenshot 5: Settings/Appearance
- Show settings screen
- Display theme customization
- Show appearance options
- Highlight customization features

#### Screenshot 6: Clipboard Manager
- Show clipboard history
- Display pinned items
- Show clipboard features
- Highlight productivity

#### Screenshot 7: Premium Features (Optional)
- Show upgrade screen
- Display premium features list
- Highlight value proposition
- Or show advanced settings

**Requirements:**
- Use actual app screenshots (not mockups)
- Add device frame (optional but recommended)
- Add captions/text overlays (optional)
- Ensure text is readable
- Follow Material Design guidelines

**Creation Method:**
1. Run app on device/emulator
2. Navigate to each screen
3. Take screenshots using:
   ```bash
   adb shell screencap -p /sdcard/screenshot.png
   adb pull /sdcard/screenshot.png
   ```
4. Edit/annotate if needed
5. Resize to 1080×1920 if needed

---

## Asset Specifications Summary

| Asset | Size | Format | Quantity | Required |
|-------|------|--------|----------|----------|
| App Icon | 512×512 | PNG | 1 | ✅ |
| Feature Graphic | 1024×500 | PNG/JPG | 1 | ✅ |
| Screenshots | 1080×1920 | PNG/JPG | 7 | ✅ |
| Promo Graphic (optional) | 180×120 | PNG | 1 | ❌ |

---

## Design Guidelines

### Colors
- **Primary:** Electric Blue (#3A7BFF)
- **Secondary:** AI Teal (#00C7B7)
- **Accent:** Magenta Pulse (#FF2BA3)
- **Background:** Pure White / Dark Charcoal
- **Avoid:** Purple and blue combinations

### Typography
- **Headings:** Inter SemiBold or Bold
- **Body:** Inter Regular
- **Sizes:** Ensure readability at small sizes

### Brand Elements
- Use logo from `branding/logo/svg/`
- Follow style guide in `branding/style-guide/UI_STYLE_GUIDE.md`
- Maintain consistency across all assets

---

## Creation Tools

### Recommended Tools
1. **Figma** - For design and layout
2. **Adobe Photoshop** - For image editing
3. **GIMP** - Free alternative
4. **Android Studio** - For device screenshots
5. **Canva** - For quick graphics

### Screenshot Tools
```bash
# Android Debug Bridge
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png

# Or use device's built-in screenshot
# Then transfer via USB or cloud
```

---

## Asset Checklist

### Before Upload
- [ ] All assets meet size requirements
- [ ] App icon is 512×512 PNG
- [ ] Feature graphic is 1024×500
- [ ] All 7 screenshots are 1080×1920
- [ ] Text is readable at small sizes
- [ ] Colors match brand guidelines
- [ ] No purple/blue combinations
- [ ] Assets are optimized (compressed)
- [ ] All text is in correct language
- [ ] No placeholder text

### Quality Check
- [ ] High resolution (no pixelation)
- [ ] Proper aspect ratios
- [ ] Consistent styling
- [ ] Professional appearance
- [ ] No typos or errors
- [ ] Brand colors correct
- [ ] Logo usage correct

---

## File Organization

```
play-store-assets/
├── app-icon-512.png
├── feature-graphic-1024x500.png
├── screenshots/
│   ├── screenshot-01-keyboard-light.png
│   ├── screenshot-02-keyboard-dark.png
│   ├── screenshot-03-voice-input.png
│   ├── screenshot-04-model-selection.png
│   ├── screenshot-05-settings.png
│   ├── screenshot-06-clipboard.png
│   └── screenshot-07-premium.png
└── README.md (this file)
```

---

## Upload Instructions

1. **Google Play Console:**
   - Navigate to **Store presence > Main store listing**
   - Upload app icon (512×512)
   - Upload feature graphic (1024×500)
   - Upload screenshots (1080×1920)

2. **Screenshot Order:**
   - Order matters - most important first
   - First screenshot is shown in search results
   - Recommended order:
     1. Main keyboard (light theme)
     2. Voice input
     3. Settings/Appearance
     4. Model selection
     5. Dark theme
     6. Clipboard
     7. Premium features

3. **Localization:**
   - Create separate sets for each language
   - Update text overlays for each locale
   - Maintain visual consistency

---

## Quick Reference

### App Icon
- Export from: `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
- Or use: `branding/logo/svg/concept-a-ai-wave-key-solid.svg`
- Resize to 512×512

### Feature Graphic Template
```
[Background: Gradient Blue to Teal]
[Logo/Icon - Left]
[App Name: AI Keyboard - Large, Bold]
[Tagline: Offline AI Voice Typing]
[Features:
  • 100% Offline Processing
  • AI-Powered Voice Typing
  • Complete Privacy
  • Fully Customizable]
```

### Screenshot Checklist
- [ ] Light theme keyboard
- [ ] Dark theme keyboard
- [ ] Voice input
- [ ] Model selection
- [ ] Settings/Appearance
- [ ] Clipboard manager
- [ ] Premium/Advanced features

---

## Notes

- All assets should be created from actual app screenshots when possible
- Mockups can be used for feature graphic if needed
- Ensure all assets reflect current app version
- Update assets when major features are added
- Test assets on different screen sizes before upload

