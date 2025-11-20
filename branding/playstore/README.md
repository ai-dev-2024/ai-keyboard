# AI Keyboard — Play Store Asset Generator

This directory contains scripts and tools to generate all Play Store visual assets for the AI Keyboard app.

## 📦 Deliverables

The generator produces:

1. **Feature Graphic** (1024 × 500px PNG)
   - Gradient background (Electric Blue → AI Teal)
   - Title: "AI Keyboard"
   - Subtitle: "Offline AI Voice Typing with Your Own Models"
   - Keyboard mockup and mic icon

2. **App Icon** (512 × 512px PNG + adaptive sizes)
   - 512×512 PNG
   - MDPI–XXXHDPI sizes (48, 72, 96, 144, 192px)

3. **Screenshots** (7 × 1080 × 1920px PNG)
   - Device frame mockups
   - Gradient backgrounds
   - Text overlays with Inter Bold
   - Brand color accents

4. **Text Overlays** (Documentation)
   - Alternative tagline options for A/B testing

## 🚀 Quick Start

### Prerequisites

```bash
pip install -r requirements.txt
```

### Generate All Assets

```bash
python generate_assets.py
```

All assets will be saved to `branding/playstore/output/`.

## 📁 Output Structure

```
output/
├── feature-graphic.png                    # 1024×500
├── app-icon-512.png                       # 512×512
├── app-icon-mdpi-48x48.png               # 48×48
├── app-icon-hdpi-72x72.png               # 72×72
├── app-icon-xhdpi-96x96.png              # 96×96
├── app-icon-xxhdpi-144x144.png           # 144×144
├── app-icon-xxxhdpi-192x192.png          # 192×192
├── screenshot-01-offline-ai-voice-typing.png
├── screenshot-02-real-time-transcription.png
├── screenshot-03-install-any-onnx-model.png
├── screenshot-04-beautiful-themes.png
├── screenshot-05-smart-typing.png
├── screenshot-06-clipboard-&-emoji.png
└── screenshot-07-support-the-project.png
```

## 🎨 Customization

### Brand Colors

Colors are defined in `generate_assets.py`:

```python
ELECTRIC_BLUE = (58, 123, 255)      # #3A7BFF
DEEP_INDIGO = (30, 42, 85)          # #1E2A55
AI_TEAL = (0, 199, 183)             # #00C7B7
SKY_MINT = (160, 255, 233)          # #A0FFE9
MAGENTA_PULSE = (255, 43, 163)      # #FF2BA3
AI_GOLD = (249, 214, 92)            # #F9D65C
```

### Fonts

The script automatically looks for Inter font in these locations:
- `branding/playstore/fonts/Inter-Bold.ttf`
- `branding/playstore/fonts/Inter-SemiBold.ttf`
- `branding/playstore/fonts/Inter-Regular.ttf`
- System fonts (Linux: `/usr/share/fonts/`, Windows: `C:/Windows/Fonts/`)

To use Inter font, download from [Google Fonts](https://fonts.google.com/specimen/Inter) and place in `branding/playstore/fonts/`.

The script will fall back to system fonts (Arial on Windows, default on Linux/macOS) if Inter is not found.

### Screenshots

Each screenshot is customizable via the `generate_screenshot()` function. The script generates 7 screenshots:

1. **Offline AI Voice Typing** - Model selection mockup
2. **Real-time Transcription** - Waveform display with mic
3. **Install Any ONNX Model** - Model cards UI
4. **Beautiful Themes** - Theme selector
5. **Smart Typing** - Keyboard with swipe trail
6. **Clipboard & Emoji** - Clipboard manager
7. **Support the Project** - Coffee/ko-fi mockup

## 📝 Screenshot Specifications

Each screenshot (1080 × 1920px) includes:

- **Device Frame**: Rounded rectangle with notch indicator
- **Screen Area**: Gradient background (Deep Indigo → Electric Blue)
- **Title**: 64px Inter Bold, white with shadow
- **Subtitle**: 36px Inter Regular, brand accent color
- **UI Mockup**: Context-specific elements based on screenshot number

## 🎯 Alternative Taglines

For A/B testing, consider these tagline variations:

- **"AI Keyboard — Privacy First"**
- **"Type. Speak. Create."**
- **"100% Offline Voice Typing"**
- **"Your AI. Your Device."**

These can be easily integrated by modifying the subtitle text in `generate_feature_graphic()` or `generate_screenshot()`.

## 🔧 Troubleshooting

### Font Not Found

If you see default fonts instead of Inter:
1. Download Inter from Google Fonts
2. Create `branding/playstore/fonts/` directory
3. Place font files there:
   - `Inter-Bold.ttf`
   - `Inter-SemiBold.ttf`
   - `Inter-Regular.ttf`

### Image Quality

All images are exported as high-quality PNGs. For web optimization, you can use tools like:
- `pngquant` for compression
- `optipng` for optimization
- ImageMagick for format conversion

### Missing Dependencies

If you get import errors:
```bash
pip install --upgrade Pillow
```

## 📋 Asset Checklist

Before publishing to Play Store:

- [ ] Feature graphic is 1024×500px (exact size)
- [ ] App icon is 512×512px (square)
- [ ] All 7 screenshots are 1080×1920px (9:16 ratio)
- [ ] Text is readable on all backgrounds
- [ ] Brand colors are accurate
- [ ] No purple/blue combinations (per brand rules)
- [ ] All text uses Inter font (or fallback)
- [ ] Device frames look realistic
- [ ] No text overlaps with UI elements

## 🎨 Design Guidelines

All assets follow the AI Keyboard style guide:

- **Primary Colors**: Electric Blue (#3A7BFF), AI Teal (#00C7B7)
- **Backgrounds**: Deep Indigo (#1E2A55), Dark Charcoal (#1D1D1F)
- **Accents**: Sky Mint (#A0FFE9), Magenta Pulse (#FF2BA3)
- **Typography**: Inter Bold for headings, Inter Regular for body
- **Shadows**: Black with 50% opacity, 3-4px offset

## 📚 Resources

- [Google Play Asset Guidelines](https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive)
- [Play Store Listing Best Practices](https://support.google.com/googleplay/android-developer/answer/9866151)
- [Inter Font](https://fonts.google.com/specimen/Inter)
- [Brand Style Guide](../style-guide/UI_STYLE_GUIDE.md)

---

**Generated by**: AI Keyboard Asset Generator  
**Last Updated**: 2024

